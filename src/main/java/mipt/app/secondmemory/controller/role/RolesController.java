package mipt.app.secondmemory.controller.role;

import mipt.app.secondmemory.dto.role.RoleDto;
import mipt.app.secondmemory.exception.file.FileNotFoundException;
import mipt.app.secondmemory.exception.session.SessionNotFoundException;
import mipt.app.secondmemory.exception.user.UserNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestBody;

public interface RolesController {
  ResponseEntity<String> addRole(@RequestBody RoleDto roleDto, @CookieValue("token") String cookieValue)
      throws UserNotFoundException, FileNotFoundException, SessionNotFoundException;

  ResponseEntity<String> removeRole(@RequestBody RoleDto roleDto, @CookieValue("token") String cookieValue)
      throws UserNotFoundException, SessionNotFoundException;
}
