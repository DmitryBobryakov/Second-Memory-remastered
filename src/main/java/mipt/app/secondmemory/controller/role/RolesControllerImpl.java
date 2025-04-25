package mipt.app.secondmemory.controller.role;

import static org.springframework.http.HttpStatus.CREATED;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mipt.app.secondmemory.dto.role.RoleDto;
import mipt.app.secondmemory.entity.FileEntity;
import mipt.app.secondmemory.exception.file.FileNotFoundException;
import mipt.app.secondmemory.exception.user.UserNotFoundException;
import mipt.app.secondmemory.repository.FilesRepository;
import mipt.app.secondmemory.service.UsersService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/roles")
public class RolesControllerImpl implements RolesController {
  private final UsersService usersService;
  private final FilesRepository filesRepository;

  @Override
  @PostMapping("/creation")
  public ResponseEntity<String> addRole(RoleDto roleDto)
      throws UserNotFoundException, FileNotFoundException {
    log.info(
        "UsersController -> addRole() -> Accepted request for adding role {} to user",
        roleDto.Role());
    FileEntity file =
        filesRepository.findById(roleDto.fileId()).orElseThrow(FileNotFoundException::new);
    usersService.addRole(roleDto.email(), file, roleDto.Role());
    return ResponseEntity.status(CREATED)
        .body(
            String.format(
                "Role %s was successfully added for user %s", roleDto.Role(), roleDto.email()));
  }

  @Override
  @PostMapping("/deletion")
  public ResponseEntity<String> removeRole(RoleDto roleDto) throws UserNotFoundException {
    log.info(
        "UsersController -> removeRole() -> Accepted request for removing role {} to user",
        roleDto.Role());
    usersService.removeRole(roleDto.email(), roleDto.fileId());
    return ResponseEntity.ok()
        .body(
            String.format(
                "Role %s was successfully removed for user %s", roleDto.Role(), roleDto.email()));
  }
}
