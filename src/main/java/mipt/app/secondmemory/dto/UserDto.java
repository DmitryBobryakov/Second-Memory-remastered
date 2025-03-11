package mipt.app.secondmemory.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserDto {
  private String email;
  private String name;

  @Override
  public String toString() {
    return "UserDto{" +
            "email='" + email + '\'' +
            ", name='" + name + '\'' +
            '}';
  }
}
