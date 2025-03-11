package mipt.app.secondmemory.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mipt.app.secondmemory.dto.RequestUserDto;
import mipt.app.secondmemory.dto.UserDto;
import mipt.app.secondmemory.entity.Session;
import mipt.app.secondmemory.entity.User;
import mipt.app.secondmemory.exception.AuthenticationDataMismatchException;
import mipt.app.secondmemory.exception.UserNotFoundException;
import mipt.app.secondmemory.repository.SessionsRepository;
import mipt.app.secondmemory.repository.UsersRepository;
import mipt.app.secondmemory.service.UsersService;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/second-memory")
public class UsersControllerImpl implements UsersController {
  private final UsersRepository usersRepository;
  private final UsersService usersService;
  private final SessionsRepository sessionsRepository;
  private final Date date = new Date();

  @Override
  @PostMapping("/signin")
  public ResponseEntity<String> authenticateUser(
      @RequestBody RequestUserDto userDto, HttpServletResponse response)
      throws UserNotFoundException, AuthenticationDataMismatchException {
    log.info(
        "UsersController -> authenticate() -> Accepted request with email {}", userDto.getEmail());
    User user = usersService.authenticate(userDto);
    log.info(
        "UsersController -> authenticate() -> Successfully authenticated with email {}",
        userDto.getEmail());

    Cookie cookie =
        new Cookie(
            "data",
            BCrypt.hashpw(String.valueOf(user.getUuid() + date.getTime()), BCrypt.gensalt()));
    cookie.setPath("/");
    cookie.setMaxAge(86400);
    cookie.setSecure(true);
    cookie.setHttpOnly(true);
    response.addCookie(cookie);
    response.setContentType("text/plain");

    sessionsRepository.save(new Session(user.getUuid(), cookie.getValue()));

    return ResponseEntity.ok("You have successfully logged in!");
  }

  @Override
  @PostMapping("/signup")
  public ResponseEntity<UserDto> registerUser(@RequestBody User user) {
    log.info(
        "UsersController -> registerUser() -> Accepted request with email {}", user.getEmail());
    usersService.create(user);
    log.info(
        "UsersController -> registerUser() -> Successfully registered with email {}",
        user.getEmail());
    return ResponseEntity.status(201).body(new UserDto(user.getEmail(), user.getName()));
  }

  @Override
  @PatchMapping("/update")
  public ResponseEntity<String> updateUser(
      @RequestBody User user, @CookieValue("data") String cookieValue)
      throws UserNotFoundException {
    log.info("UsersController -> updateUser() -> Accepted request with email {}", user.getEmail());

    Session session = sessionsRepository.findByUuid(user.getUuid());
    if (session.getCookie().equals(cookieValue)) {
      usersService.updateUser(user);
    } else {
      return ResponseEntity.status(401).body("Try to authenticate first");
    }

    log.info(
        "UsersController -> updateUser() -> Successfully updated user with email {}",
        user.getEmail());

    return ResponseEntity.ok(new UserDto(user.getEmail(), user.getPassword()).toString());
  }

  @Override
  @DeleteMapping("/delete/{username}")
  public ResponseEntity<String> deleteUser(
      @PathVariable String username,
      @RequestBody String email,
      @CookieValue("data") String cookieValue)
      throws UserNotFoundException {
    log.info("UsersController -> deleteUser() -> Accepted request with email {}", email);

    User user = usersRepository.findByEmail(email).orElseThrow();
    Session session = sessionsRepository.findByUuid(user.getUuid());
    if (session.getCookie().equals(cookieValue)) {
      usersService.deleteUser(email);
    } else {
      return ResponseEntity.status(401).body("Try to authenticate first");
    }

    log.info("UsersController -> deleteUser() -> Successfully deleted user with email {}", email);

    return ResponseEntity.ok("User " + email + " was successfully deleted");
  }

  @Override
  @PostMapping("/logout")
  public ResponseEntity<String> logOut(
      @RequestBody String requestUuid, HttpServletResponse response) {
    log.info("UsersController -> logOut() -> Accepted request user with uuid {}", requestUuid);
    Long uuid = Long.parseLong(requestUuid);
    Session session = sessionsRepository.findByUuid(uuid);
    sessionsRepository.delete(session);

    Cookie cookie = new Cookie("data", null);
    cookie.setMaxAge(0);
    cookie.setSecure(true);
    cookie.setHttpOnly(true);
    cookie.setPath("/");
    response.addCookie(cookie);

    log.info("UsersController -> logOut() -> Successfully logged out user with uuid {}", uuid);

    return ResponseEntity.ok("You have successfully log out. See you soon!");
  }
}
