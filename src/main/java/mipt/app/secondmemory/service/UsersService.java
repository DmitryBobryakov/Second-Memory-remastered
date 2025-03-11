package mipt.app.secondmemory.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mipt.app.secondmemory.dto.RequestUserDto;
import mipt.app.secondmemory.entity.User;
import mipt.app.secondmemory.exception.AuthenticationDataMismatchException;
import mipt.app.secondmemory.exception.UserAlreadyExistsException;
import mipt.app.secondmemory.exception.UserNotFoundException;
import mipt.app.secondmemory.repository.UsersRepository;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class UsersService {
  private final UsersRepository usersRepository;

  public void create(User newUser) {
    log.info("UsersService -> create() -> Accepted request with email {}", newUser.getEmail());
    usersRepository
        .findByEmail(newUser.getEmail())
        .ifPresent(
            user -> {
              throw new UserAlreadyExistsException(
                  "User with email " + user.getEmail() + " already exists");
            });
    newUser.setPassword(BCrypt.hashpw(newUser.getPassword(), BCrypt.gensalt()));
    usersRepository.save(newUser);
    log.info(
        "UsersService -> create() -> Successfully created user with email {}", newUser.getEmail());
  }

  public User authenticate(RequestUserDto user)
      throws UserNotFoundException, AuthenticationDataMismatchException {
    log.info("UsersService -> authenticate() -> Accepted request with email {}", user.getEmail());
    User dbUser =
        usersRepository.findByEmail(user.getEmail()).orElseThrow(UserNotFoundException::new);
    if (!BCrypt.checkpw(user.getPassword(), dbUser.getPassword())) {
      throw new AuthenticationDataMismatchException(
          "Wrong password for user with email " + user.getEmail());
    }
    log.info(
        "UsersService -> authenticate() -> Successfully authenticated user with email {}",
        user.getEmail());
    return dbUser;
  }

  public void updateUser(User updatedUser) throws UserNotFoundException {
    log.info(
        "UsersService -> updateUser() -> Accepted request for user with email {}",
        updatedUser.getEmail());
    User user =
        usersRepository.findByUuid(updatedUser.getUuid()).orElseThrow(UserNotFoundException::new);
    user.setName(updatedUser.getName());
    user.setPassword(BCrypt.hashpw(updatedUser.getPassword(), BCrypt.gensalt()));
    usersRepository.save(user);
    log.info(
        "UsersService -> updateUser() -> Successfully updated user with email {}", user.getEmail());
  }

  public void deleteUser(String email) throws UserNotFoundException {
    log.info("UsersService -> delete() -> Accepted request for deletion with email {}", email);
    User user = usersRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);
    usersRepository.delete(user);
    log.info("UsersService -> delete() -> Successfully deleted user with email {}", email);
  }
}
