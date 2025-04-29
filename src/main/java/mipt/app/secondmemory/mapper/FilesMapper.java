package mipt.app.secondmemory.mapper;

import jakarta.servlet.http.Part;
import java.sql.Timestamp;
import java.time.Instant;
import lombok.experimental.UtilityClass;
import mipt.app.secondmemory.dto.file.FileInfoResponse;
import mipt.app.secondmemory.entity.FileEntity;

@UtilityClass
public class FilesMapper {
  public static FileInfoResponse toFileDto(FileEntity fileEntity) {
    return FileInfoResponse.builder()
        .fileCapacity(fileEntity.getCapacity())
        .fileName(fileEntity.getName())
        .bucketId(fileEntity.getBucketId())
        .folderId(fileEntity.getFolderId())
        .fileOwnerId(fileEntity.getOwnerId())
        .fileLastModifiedDate(fileEntity.getLastModifiedTs())
        .fileCreationDate(fileEntity.getCreationTs())
        .build();
  }

  public static FileEntity toFileEntity(Part file, Long ownerId, Long bucketId, Long folderId) {
    Timestamp currentTimestamp = Timestamp.from(Instant.now());
    return FileEntity.builder()
        .capacity(file.getSize())
        .name(file.getSubmittedFileName())
        .lastModifiedTs(currentTimestamp)
        .creationTs(currentTimestamp)
        .ownerId(ownerId)
        .bucketId(bucketId)
        .folderId(folderId)
        .build();
  }
}
