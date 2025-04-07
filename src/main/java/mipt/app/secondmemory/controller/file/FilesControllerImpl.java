package mipt.app.secondmemory.controller.file;

import io.minio.Result;
import io.minio.messages.Item;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import mipt.app.secondmemory.dto.directory.DirectoryInfoRequest;
import mipt.app.secondmemory.dto.directory.RootDirectoriesRequest;
import mipt.app.secondmemory.dto.file.FileInfoRequest;
import mipt.app.secondmemory.dto.file.FileInfoResponse;
import mipt.app.secondmemory.exception.directory.NoSuchBucketException;
import mipt.app.secondmemory.exception.directory.NoSuchDirectoryException;
import mipt.app.secondmemory.exception.file.DatabaseException;
import mipt.app.secondmemory.exception.file.FileNotFoundException;
import mipt.app.secondmemory.service.FilesService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
public class FilesControllerImpl implements FilesController {
  private final FilesService filesService;

  @Override
  @SneakyThrows
  public ResponseEntity<Void> uploadSingle(String bucketName, MultipartFile file) {
    filesService.uploadSingle(bucketName, file);
    return ResponseEntity.ok().build();
  }

  @Override
  @SneakyThrows
  public ResponseEntity<Void> rename(String bucketName, String oldKey, String newKey) {
    filesService.rename(bucketName, oldKey, newKey);
    return ResponseEntity.ok().build();
  }

  @Override
  @SneakyThrows
  public ResponseEntity<Void> delete(String bucketName, String key) {
    filesService.delete(bucketName, key);
    return ResponseEntity.ok().build();
  }

  @Override
  @SneakyThrows
  public ResponseEntity<Void> moveInBucket(
      String bucketName, String fileName, String oldPath, String newPath) {
    filesService.moveInBucket(bucketName, fileName, oldPath, newPath);
    return ResponseEntity.ok().build();
  }

  @Override
  @SneakyThrows
  public ResponseEntity<Void> moveBetweenBuckets(
      String oldBucketName, String newBucketName, String key) {
    filesService.moveBetweenBuckets(oldBucketName, newBucketName, key);
    return ResponseEntity.ok().build();
  }

  @Override
  public ResponseEntity<List<FileInfoResponse>> searchFilesInDirectory(String name) {
    return ResponseEntity.ok().body(filesService.searchFiles("%" + name + "%"));
  }

  @Override
  public ResponseEntity<FileInfoResponse> getFileInfo(long fileId, FileInfoRequest fileInfoRequest)
      throws FileNotFoundException, DatabaseException {
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
