package mipt.app.secondmemory.mapper;

import mipt.app.secondmemory.dto.FileDto;
import mipt.app.secondmemory.entity.File;
import org.springframework.stereotype.Component;

@Component
public class FileMapper {
    public FileDto toDto(File file) {
        String name = file.getName();
        Long id = file.getId();
        long capacity = file.getCapacity();
        return new FileDto(id, name, capacity);
    }

    public File toFile(FileDto fIleDto) {
        return File.builder()
                .id(fIleDto.id())
                .name(fIleDto.name())
                .capacity(fIleDto.capacity())
                .build();
    }
}



