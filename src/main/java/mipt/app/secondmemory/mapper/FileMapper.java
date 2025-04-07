package mipt.app.secondmemory.mapper;

import mipt.app.secondmemory.dto.file.FileInfoResponse;
import mipt.app.secondmemory.entity.FileEntity;
import org.springframework.stereotype.Component;

@Component
public class FileMapper {
  public FileInfoResponse toDto(FileEntity fileEntity) {
    return FileInfoResponse.builder()
        .fileCapacity(fileEntity.getCapacity())
        .fileName(fileEntity.getName())
        .fileLastModifiedDate(fileEntity.getLastModifiedDate())
        .fileCreationDate(fileEntity.getCreationDate())
        .build();
  }
}
//  public FileEntity toFile(FileInfoResponse fileDto) {
//    return FileEntity.builder()
//        .id(fileDto.fileId())
//        .name(fileDto.fileName())
//        .capacity(fileDto.fileCapacity())
//        .build();
//  }

