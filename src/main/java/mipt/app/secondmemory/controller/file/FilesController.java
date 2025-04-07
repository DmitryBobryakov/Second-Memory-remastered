package mipt.app.secondmemory.controller.file;

import io.minio.Result;
import io.minio.messages.Item;
import lombok.RequiredArgsConstructor;
import mipt.app.secondmemory.dto.directory.DirectoryInfoRequest;
import mipt.app.secondmemory.dto.file.FileInfoRequest;
import mipt.app.secondmemory.dto.file.FileInfoResponse;
import mipt.app.secondmemory.dto.directory.RootDirectoriesRequest;
import mipt.app.secondmemory.exception.file.DatabaseException;
import mipt.app.secondmemory.exception.directory.NoSuchBucketException;
import mipt.app.secondmemory.exception.directory.NoSuchDirectoryException;
import mipt.app.secondmemory.exception.file.NoSuchFileException;
import mipt.app.secondmemory.service.file.FilesService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/second-memory")
public class FilesController implements FilesOperations {

  private final FilesService filesService;

  @PostMapping("/file-search")
  public ResponseEntity<List<FileInfoResponse>> searchFilesInDirectory(@RequestBody String name) {
    return ResponseEntity.ok().body(filesService.searchFiles("%" + name + "%"));
  }

  @Override
  public ResponseEntity<FileInfoResponse> getFileInfo(long fileId, FileInfoRequest fileInfoRequest)
      throws NoSuchFileException, DatabaseException {
    return ResponseEntity.ok(filesService.getFileInfo(fileId, fileInfoRequest));
  }

  @Override
  public ResponseEntity<Iterable<Result<Item>>> getFilesInDirectory(
      DirectoryInfoRequest directoryInfoRequest) throws NoSuchDirectoryException {
    return ResponseEntity.ok(filesService.getFilesInDirectory(directoryInfoRequest));
  }

  @Override
  public ResponseEntity<Iterable<Result<Item>>> getRootDirectories(
      RootDirectoriesRequest rootDirectoriesRequest) throws NoSuchBucketException {
    return ResponseEntity.ok(filesService.getRootDirectories(rootDirectoriesRequest));
  }
}
