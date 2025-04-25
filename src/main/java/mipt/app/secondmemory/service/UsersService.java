package mipt.app.secondmemory.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mipt.app.secondmemory.dto.user.AuthUserRequest;
import mipt.app.secondmemory.entity.FileEntity;
import mipt.app.secondmemory.entity.Role;
import mipt.app.secondmemory.entity.RoleType;
import mipt.app.secondmemory.entity.User;
import mipt.app.secondmemory.exception.role.NoRoleFoundException;
import mipt.app.secondmemory.exception.user.AuthenticationDataMismatchException;
import mipt.app.secondmemory.exception.user.UserAlreadyExistsException;
import mipt.app.secondmemory.exception.user.UserNotFoundException;
import mipt.app.secondmemory.repository.RolesRepository;
import mipt.app.secondmemory.repository.UsersRepository;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class UsersService {
  private final RolesRepository rolesRepository;
  private final UsersRepository usersRepository;

  public User create(User newUser) {
    log.debug("UsersService -> create() -> Accepted request with email {}", newUser.getEmail());
    usersRepository
        .findByEmail(newUser.getEmail())
        .ifPresent(
            user -> {
              throw new UserAlreadyExistsException(
                  "User with email " + user.getEmail() + " already exists");
            });
    newUser.setPassword(BCrypt.hashpw(newUser.getPassword(), BCrypt.gensalt()));
    usersRepository.save(newUser);
    log.debug(
        "UsersService -> create() -> Successfully created user with email {}", newUser.getEmail());
    return newUser;
  }

  public User authenticate(AuthUserRequest user)
      throws UserNotFoundException, AuthenticationDataMismatchException {
    log.debug("UsersService -> authenticate() -> Accepted request with email {}", user.getEmail());
    User dbUser =
        usersRepository.findByEmail(user.getEmail()).orElseThrow(UserNotFoundException::new);
    if (!BCrypt.checkpw(user.getPassword(), dbUser.getPassword())) {
      throw new AuthenticationDataMismatchException(
          "Wrong password for user with email " + user.getEmail());
    }
    log.debug(
        "UsersService -> authenticate() -> Successfully authenticated user with email {}",
        user.getEmail());
    return dbUser;
  }

  public void updateUser(User updatedUser) throws UserNotFoundException {
    log.debug(
        "UsersService -> updateUser() -> Accepted request for user with email {}",
        updatedUser.getEmail());
    User user =
        usersRepository.findById(updatedUser.getId()).orElseThrow(UserNotFoundException::new);
    user.setName(updatedUser.getName());
    user.setPassword(BCrypt.hashpw(updatedUser.getPassword(), BCrypt.gensalt()));
    usersRepository.save(user);
    log.debug(
        "UsersService -> updateUser() -> Successfully updated user with email {}", user.getEmail());
  }

  public void deleteUser(String email) throws UserNotFoundException {
    log.debug("UsersService -> delete() -> Accepted request for deletion with email {}", email);
    User user = usersRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);
    usersRepository.delete(user);
    log.debug("UsersService -> delete() -> Successfully deleted user with email {}", email);
  }

  @Transactional
  public void addRole(String email, FileEntity file, RoleType roleType)
      throws UserNotFoundException {
    log.info("UsersService -> addRole() -> Accepted request for adding role");
    User user = usersRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);
    Role newRole = new Role(user, file, roleType);
    user.getRoles().add(newRole);
    usersRepository.save(user);
  }

  @Transactional
  public void removeRole(String email, Long fileId) throws UserNotFoundException {
    log.info("UsersService -> removeRole() -> Accepted request for removing role");
    User user = usersRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);
    Role role =
        rolesRepository
            .findByUserIdAndFileId(user.getId(), fileId)
            .orElseThrow(NoRoleFoundException::new);
    user.getRoles().remove(role);
    usersRepository.save(user);
  }
}
