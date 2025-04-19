package mipt.app.secondmemory.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.proxy.HibernateProxy;

@Entity
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "files_info")
@Schema(name = "File", description = "Сущность Файла")
public class FileEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;

  @Column(name = "name", nullable = false, length = 50)
  @NotNull(message = "File name have to be filled")
  @Schema(description = "Имя файла", example = "Red Hat.png", type = "String")
  private String name;

  @Column(name = "capacity", nullable = false)
  @NotNull(message = "File capacity have to be filled")
  @Schema(description = "Размер файла в байтах", example = "1024", type = "long")
  private long capacity;

  @Schema(description = "Id пользователя, который создал данный файл", example = "1", type = "Long")
  @Column(name = "owner_id")
  @NotNull(message = "File owner ID has to be filled")
  private Long ownerId;

  @Schema(description = "Id бакета, в котором хранится данный файл", example = "12", type = "Long")
  @Column(name = "bucket_id")
  @NotNull
  private Long bucketId;

  @Schema(description = "Дата создания файла", type = "Timestamp")
  @Column(name = "creation_ts")
  @NotNull(message = "File creation date has to be filled")
  private Timestamp creationDate;

  @Schema(description = "Последняя дата обновления файла", type = "Timestamp")
  @Column(name = "last_modified_ts")
  @NotNull(message = "File last modified date has to be filled")
  private Timestamp lastModifiedDate;

  @Override
  public final boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null) {
      return false;
    }
    Class<?> oEffectiveClass =
        o instanceof HibernateProxy
            ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass()
            : o.getClass();
    Class<?> thisEffectiveClass =
        this instanceof HibernateProxy
            ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass()
            : this.getClass();
    if (thisEffectiveClass != oEffectiveClass) {
      return false;
    }
    FileEntity that = (FileEntity) o;
    return getId() != null && Objects.equals(getId(), that.getId());
  }

  @Override
  public final int hashCode() {
    return this instanceof HibernateProxy
        ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode()
        : getClass().hashCode();
  }

  @Override
  public String toString() {
    return getClass().getSimpleName()
        + "("
        + "id = "
        + id
        + ", "
        + "name = "
        + name
        + ", "
        + "capacity = "
        + capacity
        + ", "
        + "ownerId = "
        + ownerId
        + ", "
        + "creationDate = "
        + creationDate
        + ", "
        + "lastModifiedDate = "
        + lastModifiedDate
        + ", "
        + "bucketId = "
        + bucketId
        + ")";
  }
}
