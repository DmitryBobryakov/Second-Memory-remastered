package mipt.app.secondmemory.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "users")
@NoArgsConstructor
@Getter
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long uuid;

  @Email(message = "Email should be valid")
  private String email;

  @NotNull(message = "Name should be filled")
  @Setter
  private String name;

  @NotNull(message = "Password should be filled")
  @Setter
  private String password;

  public User(String email, String name, String password) {
    this.email = email;
    this.name = name;
    this.password = password;
  }
}
