package mipt.app.secondmemory.mapper;

import lombok.experimental.UtilityClass;
import jakarta.servlet.http.Part;
import lombok.experimental.UtilityClass;
import mipt.app.secondmemory.dto.file.FileInfoResponse;
import mipt.app.secondmemory.entity.FileEntity;
import java.sql.Timestamp;
import java.time.Instant;

@UtilityClass
public class FileMapper {
  public static FileInfoResponse toFileDto(FileEntity fileEntity) {
    return FileInfoResponse.builder()
        .fileCapacity(fileEntity.getCapacity())
        .fileName(fileEntity.getName())
        .fileLastModifiedDate(fileEntity.getLastModifiedDate())
        .fileCreationDate(fileEntity.getCreationDate())
        .build();
  }

  public static FileEntity toFileEntity(Part file, Long ownerId, Long bucketId, Long folderId) {
    Timestamp currentTimestamp = Timestamp.from(Instant.now());
    return FileEntity.builder()
        .capacity(file.getSize())
        .name(file.getSubmittedFileName())
        .lastModifiedDate(currentTimestamp)
        .creationDate(currentTimestamp)
        .ownerId(ownerId)
        .bucketId(bucketId)
        .folderId(folderId)
        .build();
  }
}
