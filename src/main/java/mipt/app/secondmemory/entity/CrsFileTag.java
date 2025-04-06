package mipt.app.secondmemory.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import mipt.app.secondmemory.entity.id.FileTagId;
import org.hibernate.proxy.HibernateProxy;

import java.util.Objects;

@Table(name = "files_tags")
@Entity
@IdClass(FileTagId.class)
@Getter
@Setter
public class CrsFileTag {
  @Id private Long fileId;

  @Id private Long tagId;

  @Override
  public final boolean equals(Object o) {
    if (this == o) return true;
    if (o == null) return false;
    Class<?> oEffectiveClass =
        o instanceof HibernateProxy
            ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass()
            : o.getClass();
    Class<?> thisEffectiveClass =
        this instanceof HibernateProxy
            ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass()
            : this.getClass();
    if (thisEffectiveClass != oEffectiveClass) return false;
    CrsFileTag that = (CrsFileTag) o;
    return getFileId() != null
        && Objects.equals(getFileId(), that.getFileId())
        && getTagId() != null
        && Objects.equals(getTagId(), that.getTagId());
  }

  @Override
  public final int hashCode() {
    return Objects.hash(fileId, tagId);
  }

  @Override
  public String toString() {
    return getClass().getSimpleName()
        + "("
        + "fileId = "
        + fileId
        + ", "
        + "tagId = "
        + tagId
        + ")";
  }
}
