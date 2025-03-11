package mipt.app.secondmemory.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Entity
@Table(name = "files_info")
@Getter
public class File {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "file_id")
  private long fileId;

  @Column(name = "file_name")
  @NotNull(message = "File name has to be filled")
  private String fileName;

  @Column(name = "file_owner_id")
  @NotNull(message = "File owner ID has to be filled")
  private String fileOwnerId;

  @Column(name = "file_creation_date")
  @NotNull(message = "File creation date has to be filled")
  private String fileCreationDate;

  @Column(name = "file_last_modified_date")
  @NotNull(message = "File last modified date has to be filled")
  private String fileLastModifiedDate;

  @Column(name = "file_access_level")
  @NotNull(message = "File access level has to be filled")
  private String fileAccessLevel;

  @Column(name = "file_tags")
  private String fileTags;

  protected File() {}

  public File(String fileName, String fileOwnerId, String fileCreationDate, String fileLastModifiedDate, String fileAccessLevel, String fileTags) {
    this.fileName = fileName;
    this.fileOwnerId = fileOwnerId;
    this.fileCreationDate = fileCreationDate;
    this.fileLastModifiedDate = fileLastModifiedDate;
    this.fileAccessLevel = fileAccessLevel;
    this.fileTags = fileTags;
  }
}
