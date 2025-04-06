package mipt.app.secondmemory.mapper;

import mipt.app.secondmemory.dto.FileMinioDto;
import mipt.app.secondmemory.entity.FileEntity;
import org.springframework.stereotype.Component;

@Component
public class FileMapper {
  public FileMinioDto toDto(FileEntity fileEntity) {
    String name = fileEntity.getName();
    Long id = fileEntity.getId();
    long capacity = fileEntity.getCapacity();
    return new FileMinioDto(id, name, capacity);
  }

  public FileEntity toFile(FileMinioDto fIleMinioDto) {
    return FileEntity.builder()
        .id(fIleMinioDto.id())
        .name(fIleMinioDto.name())
        .capacity(fIleMinioDto.capacity())
        .build();
  }
}
