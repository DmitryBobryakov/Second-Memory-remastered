package mipt.app.secondmemory.entity;

import jakarta.persistence.Column;
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

  @NotNull
  @Column(name = "name")
  private String name;

  @NotNull
  @Column(name = "owner_id")
  private Long ownerId;

  @NotNull
  @Column(name = "creation_date")
  private Instant creationDate;

  @NotNull
  @Column(name = "last_modified_date")
  private Instant lastModifiedDate;

  @NotNull
  @Column(name = "access_level")
  private String accessLevel;

  @Column(name = "tags")
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
