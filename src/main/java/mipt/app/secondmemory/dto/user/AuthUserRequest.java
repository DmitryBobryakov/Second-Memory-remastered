package mipt.app.secondmemory.dto.user;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class AuthUserRequest {
  private String email;
  private String password;
}
