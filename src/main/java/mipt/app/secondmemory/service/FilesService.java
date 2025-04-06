package mipt.app.secondmemory.service;

import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mipt.app.secondmemory.dto.FileDto;
import mipt.app.secondmemory.entity.File;
import mipt.app.secondmemory.repository.FilesRepository;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class FilesService {
  private final FilesRepository filesRepository;

  public List<FileDto> searchFiles(String name) {
    List<File> files = filesRepository.findByNameLike(name);
    List<FileDto> resultFiles = new ArrayList<>();
    for (File file : files) {
      resultFiles.add(new FileDto(file));
    }
    return resultFiles;
  }
}
