package mipt.app.secondmemory.controller.file;

import static mipt.app.secondmemory.entity.RoleType.OWNER;
import static mipt.app.secondmemory.entity.RoleType.READER;
import static mipt.app.secondmemory.entity.RoleType.WRITER;

import io.minio.Result;
import io.minio.errors.ErrorResponseException;
import io.minio.errors.InsufficientDataException;
import io.minio.errors.InternalException;
import io.minio.errors.InvalidResponseException;
import io.minio.errors.ServerException;
import io.minio.errors.XmlParserException;
import io.minio.messages.Item;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import mipt.app.secondmemory.dto.directory.DirectoryInfoRequest;
import mipt.app.secondmemory.dto.directory.RootDirectoriesRequest;
import mipt.app.secondmemory.dto.file.FileInfoRequest;
import mipt.app.secondmemory.dto.file.FileInfoResponse;
import mipt.app.secondmemory.entity.Role;
import mipt.app.secondmemory.entity.Session;
import mipt.app.secondmemory.exception.directory.NoSuchBucketException;
import mipt.app.secondmemory.exception.directory.NoSuchDirectoryException;
import mipt.app.secondmemory.exception.file.DatabaseException;
import mipt.app.secondmemory.exception.file.FileNotFoundException;
import mipt.app.secondmemory.exception.role.NoRoleFoundException;
import mipt.app.secondmemory.exception.session.SessionNotFoundException;
import mipt.app.secondmemory.repository.RolesRepository;
import mipt.app.secondmemory.repository.SessionsRepository;
import mipt.app.secondmemory.service.FilesService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class FilesControllerImpl implements FilesController {
  private final FilesService filesService;
  private final RolesRepository rolesRepository;
  private final SessionsRepository sessionsRepository;

  @Override
  @SneakyThrows
  public ResponseEntity<Void> uploadSingle(String bucketName, MultipartFile file) {
    filesService.uploadSingle(bucketName, file);
    return ResponseEntity.ok().build();
  }

  @Override
  public ResponseEntity<Void> rename(
      String bucketName, Long fileId, String oldKey, String newKey, String cookieValue)
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
          SessionNotFoundException {
    Session session =
        sessionsRepository.findByCookie(cookieValue).orElseThrow(SessionNotFoundException::new);
    Role userRole =
        rolesRepository
            .findByUserIdAndFileId(session.getUser().getId(), fileId)
            .orElseThrow(NoRoleFoundException::new);
    if (userRole.getType() != WRITER && userRole.getType() != OWNER) {
      throw new AuthorizationDeniedException("You don't have permission to rename file");
    }
    filesService.rename(bucketName, oldKey, newKey);
    return ResponseEntity.ok().build();
  }

  @Override
  @SneakyThrows
  public ResponseEntity<Void> delete(
      String bucketName, Long fileId, String key, String cookieValue) {
    Session session =
        sessionsRepository.findByCookie(cookieValue).orElseThrow(SessionNotFoundException::new);
    Role userRole =
        rolesRepository
            .findByUserIdAndFileId(session.getUser().getId(), fileId)
            .orElseThrow(NoRoleFoundException::new);
    if (userRole.getType() != OWNER) {
      throw new AuthorizationDeniedException("You don't have permission to delete file");
    }
    filesService.delete(bucketName, key);
    return ResponseEntity.ok().build();
  }

  @Override
  @SneakyThrows
  public ResponseEntity<Void> moveInBucket(
      String bucketName,
      Long fileId,
      String fileName,
      String oldPath,
      String newPath,
      String cookieValue) {
    Session session =
        sessionsRepository.findByCookie(cookieValue).orElseThrow(SessionNotFoundException::new);
    Role userRole =
        rolesRepository
            .findByUserIdAndFileId(session.getUser().getId(), fileId)
            .orElseThrow(NoRoleFoundException::new);
    if (userRole.getType() != OWNER) {
      throw new AuthorizationDeniedException("You don't have permission to move file");
    }
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
  public ResponseEntity<FileInfoResponse> getFileInfo(
      long fileId, FileInfoRequest fileInfoRequest, String cookieValue)
      throws FileNotFoundException, DatabaseException, SessionNotFoundException {
    Session session =
        sessionsRepository.findByCookie(cookieValue).orElseThrow(SessionNotFoundException::new);
    Role userRole =
        rolesRepository
            .findByUserIdAndFileId(session.getUser().getId(), fileId)
            .orElseThrow(NoRoleFoundException::new);
    if (userRole.getType() != READER
        && userRole.getType() != WRITER
        && userRole.getType() != OWNER) {
      throw new AuthorizationDeniedException("You don't have permission to read file information");
    }
    return ResponseEntity.ok(filesService.getFileInfo(fileId, fileInfoRequest));
  }

  @Override
  public ResponseEntity<Iterable<Result<Item>>> getFilesInDirectory(
      DirectoryInfoRequest directoryInfoRequest, String cookieValue)
      throws NoSuchDirectoryException {
    return ResponseEntity.ok(filesService.getFilesInDirectory(directoryInfoRequest));
  }

  @Override
  public ResponseEntity<Iterable<Result<Item>>> getRootDirectories(
      RootDirectoriesRequest rootDirectoriesRequest, String cookieValue)
      throws NoSuchBucketException {
    return ResponseEntity.ok(filesService.getRootDirectories(rootDirectoriesRequest));
  }
}
