package mipt.app.secondmemory.mapper;

import lombok.experimental.UtilityClass;
import mipt.app.secondmemory.dto.file.FileInfoResponse;
import mipt.app.secondmemory.entity.FileEntity;

@UtilityClass
public class FilesMapper {
  public static FileInfoResponse toDto(FileEntity fileEntity) {
    return FileInfoResponse.builder()
        .fileCapacity(fileEntity.getCapacity())
        .fileName(fileEntity.getName())
        .fileLastModifiedDate(fileEntity.getLastModifiedDate())
        .fileCreationDate(fileEntity.getCreationDate())
        .build();
  }
}
