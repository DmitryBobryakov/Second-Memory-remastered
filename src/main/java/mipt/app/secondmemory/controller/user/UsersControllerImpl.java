package mipt.app.secondmemory.controller.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import java.time.Instant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mipt.app.secondmemory.dto.message.MessageDto;
import mipt.app.secondmemory.dto.message.MessageType;
import mipt.app.secondmemory.dto.user.AuthUserRequest;
import mipt.app.secondmemory.dto.user.RegisterUserResponse;
import mipt.app.secondmemory.entity.Session;
import mipt.app.secondmemory.entity.User;
import mipt.app.secondmemory.exception.session.SessionNotFoundException;
import mipt.app.secondmemory.exception.user.AuthenticationDataMismatchException;
import mipt.app.secondmemory.exception.user.UserNotFoundException;
import mipt.app.secondmemory.repository.FilesRepository;
import mipt.app.secondmemory.repository.SessionsRepository;
import mipt.app.secondmemory.repository.UsersRepository;
import mipt.app.secondmemory.service.KafkaProducerService;
import mipt.app.secondmemory.service.UsersService;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UsersControllerImpl implements UsersController {
  private final UsersRepository usersRepository;
  private final UsersService usersService;
  private final SessionsRepository sessionsRepository;
  private final KafkaProducerService producerService;
  private final Instant date = Instant.now();

  @Override
  @PostMapping("/sign-in")
  public ResponseEntity<String> authenticateUser(
      AuthUserRequest userDto, HttpServletResponse response)
      throws UserNotFoundException, AuthenticationDataMismatchException, JsonProcessingException {
    log.debug(
        "UsersController -> authenticate() -> Accepted request with email {}", userDto.getEmail());
    User user = usersService.authenticate(userDto);
    log.debug(
        "UsersController -> authenticate() -> Successfully authenticated with email {}",
        userDto.getEmail());

    Cookie cookie =
        new Cookie(
            "token",
            BCrypt.hashpw(String.valueOf(user.getId() + date.getEpochSecond()), BCrypt.gensalt()));
    cookie.setPath("/");
    cookie.setMaxAge(86400);
    cookie.setSecure(true);
    cookie.setHttpOnly(true);
    response.addCookie(cookie);
    response.setContentType("text/plain");

    try {
      Session session =
          sessionsRepository.findByUserId(user.getId()).orElseThrow(SessionNotFoundException::new);
      session.setCookie(cookie.getValue());
      sessionsRepository.save(session);
    } catch (SessionNotFoundException e) {
      sessionsRepository.save(new Session(cookie.getValue(), user));
    }

    producerService.sendMessage(
        new MessageDto(user.getEmail(), user.getName(), MessageType.AUTHENTICATION));

    return ResponseEntity.ok("You have successfully logged in!");
  }

  @Override
  @PostMapping("/sign-up")
  public ResponseEntity<RegisterUserResponse> registerUser(User user)
      throws JsonProcessingException {
    log.debug(
        "UsersController -> registerUser() -> Accepted request with email {}", user.getEmail());
    usersService.create(user);
    log.debug(
        "UsersController -> registerUser() -> Successfully registered with email {}",
        user.getEmail());

    producerService.sendMessage(
        new MessageDto(user.getEmail(), user.getName(), MessageType.REGISTRATION));
    return ResponseEntity.status(201)
        .body(new RegisterUserResponse(user.getEmail(), user.getName()));
  }

  @Override
  @PatchMapping("/update")
  public ResponseEntity<String> updateUser(User user, String cookieValue)
      throws UserNotFoundException {
    log.debug("UsersController -> updateUser() -> Accepted request with email {}", user.getEmail());

    Session session = sessionsRepository.findByUserId(user.getId()).orElseThrow();
    if (session.getCookie().equals(cookieValue)) {
      usersService.updateUser(user);
    } else {
      return ResponseEntity.status(401).body("Try to authenticate first");
    }

    log.debug(
        "UsersController -> updateUser() -> Successfully updated user with email {}",
        user.getEmail());

    return ResponseEntity.ok(String.format("Data for %s was successfully updated", user.getName()));
  }

  @Override
  @DeleteMapping("/delete")
  public ResponseEntity<String> deleteUser(String email, String cookieValue)
      throws UserNotFoundException, JsonProcessingException {
    log.debug("UsersController -> deleteUser() -> Accepted request with email {}", email);

    User user = usersRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);
    Session session = sessionsRepository.findByUserId(user.getId()).orElseThrow();
    if (session.getCookie().equals(cookieValue)) {
      usersService.deleteUser(email);
    } else {
      return ResponseEntity.status(401).body("Try to authenticate first");
    }

    log.debug("UsersController -> deleteUser() -> Successfully deleted user with email {}", email);

    producerService.sendMessage(new MessageDto(email, user.getName(), MessageType.DELETION));

    return ResponseEntity.ok("User " + email + " was successfully deleted");
  }

  @Override
  @PostMapping("/logout")
  public ResponseEntity<String> logOut(String requestUuid, HttpServletResponse response)
      throws UserNotFoundException {
    log.debug("UsersController -> logOut() -> Accepted request user with uuid {}", requestUuid);
    Long uuid = Long.parseLong(requestUuid);
    Session session = sessionsRepository.findByUserId(uuid).orElseThrow(UserNotFoundException::new);
    sessionsRepository.delete(session);

    Cookie cookie = new Cookie("token", null);
    cookie.setMaxAge(0);
    cookie.setSecure(true);
    cookie.setHttpOnly(true);
    cookie.setPath("/");
    response.addCookie(cookie);

    log.debug("UsersController -> logOut() -> Successfully logged out user with uuid {}", uuid);

    return ResponseEntity.ok("You have successfully log out. See you soon!");
  }
}
