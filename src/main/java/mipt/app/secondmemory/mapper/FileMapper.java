package mipt.app.secondmemory.mapper;

import mipt.app.secondmemory.dto.FileMinioDto;
import mipt.app.secondmemory.entity.File;
import org.springframework.stereotype.Component;

@Component
public class FileMapper {
  public FileMinioDto toDto(File file) {
    String name = file.getName();
    Long id = file.getId();
    long capacity = file.getCapacity();
    return new FileMinioDto(id, name, capacity);
  }

  public File toFile(FileMinioDto fIleMinioDto) {
    return File.builder()
        .id(fIleMinioDto.id())
        .name(fIleMinioDto.name())
        .capacity(fIleMinioDto.capacity())
        .build();
  }
}
