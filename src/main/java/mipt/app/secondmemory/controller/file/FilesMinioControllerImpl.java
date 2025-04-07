package mipt.app.secondmemory.controller.file;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import mipt.app.secondmemory.service.file.FilesMinioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@Slf4j
@RequiredArgsConstructor
public class FilesMinioControllerImpl implements FilesMinioController {
  private final FilesMinioService filesService;

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
}
