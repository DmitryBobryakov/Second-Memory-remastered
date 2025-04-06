package mipt.app.secondmemory.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mipt.app.secondmemory.dto.FileDto;
import mipt.app.secondmemory.service.FilesService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/second-memory")
public class FilesController {
  private final FilesService filesService;

  @PostMapping("/file-search")
  public ResponseEntity<List<FileDto>> searchFilesInDirectory(@RequestBody String name) {
    return ResponseEntity.ok().body(filesService.searchFiles("%" + name + "%"));
  }
}
