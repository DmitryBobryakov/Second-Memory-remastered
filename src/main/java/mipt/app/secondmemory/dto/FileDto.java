package mipt.app.secondmemory.dto;

import lombok.Data;
import mipt.app.secondmemory.entity.File;

import java.time.Instant;

@Data
public class FileDto {
  private String name;
  private Long ownerId;
  private Instant creationDate;
  private Instant lastModifiedDate;
  private String accessLevel;
  private String tags;

  public FileDto(File file) {
    this.name = file.getName();
    this.ownerId = file.getOwnerId();
    this.creationDate = file.getCreationDate();
    this.lastModifiedDate = file.getLastModifiedDate();
    this.accessLevel = file.getAccessLevel();
    this.tags = file.getTags();
  }
}
