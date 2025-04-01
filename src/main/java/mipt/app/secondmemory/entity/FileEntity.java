package mipt.app.secondmemory.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import java.sql.Timestamp;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "files_info")
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class FileEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private long id;

  @Column(name = "name")
  @NotNull(message = "File name has to be filled")
  private String name;

  @Column(name = "owner_id")
  @NotNull(message = "File owner ID has to be filled")
  private long ownerId;

  @Column(name = "creation_date")
  @NotNull(message = "File creation date has to be filled")
  private Timestamp creationDate;

  @Column(name = "last_modified_date")
  @NotNull(message = "File last modified date has to be filled")
  private Timestamp lastModifiedDate;

  @Column(name = "access_level_id")
  @NotNull(message = "File access level has to be filled")
  private long accessLevelId;

  @Override
  public String toString() {
    return STR."ID: \{id}, Name: \{name}, OwnerId: \{ownerId}, CreationDate: \{creationDate}, LastModifiedDate: \{lastModifiedDate}, AccessLevelId: \{accessLevelId}";
  }

  @Override
  public boolean equals(Object o) {
    return this.id == ((FileEntity) o).id;
  }

  @Override
  public int hashCode() {
    return Long.hashCode(id);
  }
}
