package mipt.app.secondmemory.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import java.time.Instant;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "files")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
public class File {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotNull private String name;
  @NotNull private Long ownerId;
  @NotNull private Instant creationDate;
  @NotNull private Instant lastModifiedDate;
  @NotNull private String accessLevel;
  private String tags;

  public File(
      String name,
      Long ownerId,
      Instant creationDate,
      Instant lastModifiedDate,
      String accessLevel,
      String tags) {
    this.name = name;
    this.ownerId = ownerId;
    this.creationDate = creationDate;
    this.lastModifiedDate = lastModifiedDate;
    this.accessLevel = accessLevel;
    this.tags = tags;
  }
}
