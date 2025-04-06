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
import org.hibernate.proxy.HibernateProxy;

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
  private Long id;

  @Column(name = "name")
  @NotNull(message = "File name has to be filled")
  private String name;

  @Column(name = "capacity", nullable = false)
  @NotNull(message = "File capacity have to be filled")
  @Schema(description = "Размер файла в байтах", example = "1024", type = "long")
  private long capacity;

  @Column(name = "owner_id")
  @NotNull(message = "File owner ID has to be filled")
  private Long ownerId;

  @Column(name = "creation_date")
  @NotNull(message = "File creation date has to be filled")
  private Timestamp creationDate;

  @Column(name = "last_modified_date")
  @NotNull(message = "File last modified date has to be filled")
  private Timestamp lastModifiedDate;

  @Schema(description = "Id бакета, в котором хранится данный файл", example = "12", type = "Long")
  @Column(name = "bucket_id")
  private Long bucketId;

  @Override
  public final boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null) {
      return false;
    }
    Class<?> oEffectiveClass = o instanceof HibernateProxy
        ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass()
        : o.getClass();
    Class<?> thisEffectiveClass = this instanceof HibernateProxy
        ? ((HibernateProxy) this).getHibernateLazyInitializer()
        .getPersistentClass() : this.getClass();
    if (thisEffectiveClass != oEffectiveClass) {
      return false;
    }
    FileEntity that = (FileEntity) o;
    return getId() != null && Objects.equals(getId(), that.getId());
  }

  @Override
  public final int hashCode() {
    return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer()
        .getPersistentClass().hashCode() : getClass().hashCode();
  }

  @Override
  public String toString() {
    return getClass().getSimpleName() + "(" +
        "id = " + id + ", " +
        "name = " + name + ", " +
        "capacity = " + capacity + ", " +
        "ownerId = " + ownerId + ", " +
        "creationDate = " + creationDate + ", " +
        "lastModifiedDate = " + lastModifiedDate + ", " +
        "bucketId = " + bucketId + ")";
  }
}
