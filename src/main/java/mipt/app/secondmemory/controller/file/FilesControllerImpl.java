package mipt.app.secondmemory.controller.file;

import io.minio.Result;
import io.minio.errors.ErrorResponseException;
import io.minio.errors.InsufficientDataException;
import io.minio.errors.InternalException;
import io.minio.errors.InvalidResponseException;
import io.minio.errors.ServerException;
import io.minio.errors.XmlParserException;
import io.minio.messages.Item;
import jakarta.servlet.http.Part;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mipt.app.secondmemory.dto.directory.DirectoryInfoRequest;
import mipt.app.secondmemory.dto.directory.RootDirectoriesRequest;
import mipt.app.secondmemory.dto.file.FileInfoRequest;
import mipt.app.secondmemory.dto.file.FileInfoResponse;
import mipt.app.secondmemory.exception.directory.NoSuchBucketException;
import mipt.app.secondmemory.exception.directory.NoSuchDirectoryException;
import mipt.app.secondmemory.exception.file.DatabaseException;
import mipt.app.secondmemory.exception.file.FileAlreadyExistsException;
import mipt.app.secondmemory.exception.file.FileMemoryLimitExceededException;
import mipt.app.secondmemory.exception.file.FileNotFoundException;
import mipt.app.secondmemory.service.FilesService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class FilesControllerImpl implements FilesController {
  private final FilesService filesService;

  @Override
  public ResponseEntity<Void> uploadFiles(String bucketName, Part file)
      throws ServerException,
          InsufficientDataException,
          ErrorResponseException,
          IOException,
          NoSuchAlgorithmException,
          FileMemoryLimitExceededException,
          InvalidKeyException,
          NoSuchBucketException,
          InvalidResponseException,
          XmlParserException,
          InternalException {
    filesService.uploadFile(bucketName, file);
    return ResponseEntity.ok().build();
  }

  @Override
  public ResponseEntity<Void> renameFile(String bucketName, String oldKey, String newKey)
      throws ServerException,
          InsufficientDataException,
          FileNotFoundException,
          ErrorResponseException,
          IOException,
          NoSuchAlgorithmException,
          InvalidKeyException,
          InvalidResponseException,
          XmlParserException,
          InternalException {
    filesService.renameFile(bucketName, oldKey, newKey);
    return ResponseEntity.ok().build();
  }

  @Override
  public ResponseEntity<Void> deleteFile(String bucketName, String key)
      throws ServerException,
          InsufficientDataException,
          FileNotFoundException,
          ErrorResponseException,
          IOException,
          NoSuchAlgorithmException,
          InvalidKeyException,
          InvalidResponseException,
          XmlParserException,
          InternalException {
    filesService.deleteFile(bucketName, key);
    return ResponseEntity.ok().build();
  }

  @Override
  public ResponseEntity<Void> moveFile(
      String oldBucketName, String newBucketName, String fileName, String oldPath, String newPath)
      throws ServerException,
          InsufficientDataException,
          FileNotFoundException,
          ErrorResponseException,
          IOException,
          NoSuchAlgorithmException,
          InvalidKeyException,
          InvalidResponseException,
          XmlParserException,
          InternalException,
          FileAlreadyExistsException,
          NoSuchBucketException {
    filesService.moveFile(oldBucketName, newBucketName, fileName, oldPath, newPath);
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
