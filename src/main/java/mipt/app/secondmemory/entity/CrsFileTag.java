package mipt.app.secondmemory.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;

@Table(name = "files_tags")
@Entity
@IdClass(FileTagId.class)
public class CrsFileTag {
    @Id
    private Long fileId;

    @Id
    private Long tagId;

}
