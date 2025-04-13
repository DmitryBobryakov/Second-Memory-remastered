package mipt.app.secondmemory.entity;

import static jakarta.persistence.CascadeType.ALL;
import static jakarta.persistence.CascadeType.MERGE;
import static jakarta.persistence.CascadeType.PERSIST;
import static jakarta.persistence.FetchType.EAGER;
import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@NoArgsConstructor(access = PROTECTED)
@Getter
@Setter
public class User {
  @Id
  @GeneratedValue(strategy = IDENTITY)
  private Long id;

  @Email(message = "Email should be valid")
  @Column(name = "email")
  private String email;

  @NotNull(message = "Name should be filled")
  @Column(name = "name")
  private String name;

  @NotNull(message = "Password should be filled")
  @Column(name = "password")
  private String password;

  @OneToOne(mappedBy = "user", cascade = ALL, fetch = EAGER)
  private Session session;

  @OneToMany(fetch = LAZY, mappedBy = "user", cascade = ALL, orphanRemoval = true)
  private List<Role> roles = new ArrayList<>();

  public User(String email, String name, String password) {
    this.email = email;
    this.name = name;
    this.password = password;
  }
}
