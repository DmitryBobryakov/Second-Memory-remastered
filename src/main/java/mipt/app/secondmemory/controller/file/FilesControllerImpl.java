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
import mipt.app.secondmemory.entity.Role;
import mipt.app.secondmemory.entity.Session;
import mipt.app.secondmemory.exception.directory.BucketNotFoundException;
import mipt.app.secondmemory.exception.directory.NoSuchBucketException;
import mipt.app.secondmemory.exception.directory.NoSuchDirectoryException;
import mipt.app.secondmemory.exception.file.DatabaseException;
import mipt.app.secondmemory.exception.file.FileAlreadyExistsException;
import mipt.app.secondmemory.exception.file.FileMemoryLimitExceededException;
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

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class FilesControllerImpl implements FilesController {
  private final FilesService filesService;
  private final RolesRepository rolesRepository;
  private final SessionsRepository sessionsRepository;

  @Override
  public ResponseEntity<FileInfoResponse> uploadFile(Long bucketId, Part file, String cookieValue)
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
    Session session =
        sessionsRepository.findByCookie(cookieValue).orElseThrow(SessionNotFoundException::new);
    return ResponseEntity.ok(filesService.uploadFile(bucketId, file, session.getUser()));
  }

  @Override
  public ResponseEntity<FileInfoResponse> renameFile(
      Long fileId, String newFileName, String cookieValue)
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
          NoSuchDirectoryException,
          BucketNotFoundException {
    //    return ResponseEntity.ok(filesService.renameFile(fileId, newFileName));
    //          InternalException,
    //          SessionNotFoundException {
    Session session =
        sessionsRepository.findByCookie(cookieValue).orElseThrow(SessionNotFoundException::new);
    Role userRole =
        rolesRepository
            .findByUserIdAndFileId(session.getUser().getId(), fileId)
            .orElseThrow(NoRoleFoundException::new);
    if (userRole.getType() != WRITER && userRole.getType() != OWNER) {
      throw new AuthorizationDeniedException("You don't have permission to rename file");
    }
    return ResponseEntity.ok(filesService.renameFile(fileId, newFileName));
    //    filesService.renameFile(bucketName, oldKey, newKey);
    //    return ResponseEntity.ok().build();
  }

  @Override
  public ResponseEntity<FileInfoResponse> deleteFile(Long fileId, String cookieValue)
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
          NoSuchDirectoryException,
          BucketNotFoundException {

    //    return ResponseEntity.ok(filesService.deleteFile(fileId));
    //          InternalException,
    //          SessionNotFoundException {
    Session session =
        sessionsRepository.findByCookie(cookieValue).orElseThrow(SessionNotFoundException::new);
    Role userRole =
        rolesRepository
            .findByUserIdAndFileId(session.getUser().getId(), fileId)
            .orElseThrow(NoRoleFoundException::new);
    if (userRole.getType() != OWNER) {
      throw new AuthorizationDeniedException("You don't have permission to delete file");
    }
    //    filesService.deleteFile(bucketName, key);
    return ResponseEntity.ok(filesService.deleteFile(fileId));
    //    return ResponseEntity.ok().build();
  }

  @Override
  public ResponseEntity<FileInfoResponse> moveFile(Long fileId, Long folderId, String cookieValue)
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
          NoSuchBucketException,
          NoSuchDirectoryException,
          BucketNotFoundException {

    //    return ResponseEntity.ok(filesService.moveFile(fileId, folderId));
    //          NoSuchBucketException,
    //          SessionNotFoundException {
    Session session =
        sessionsRepository.findByCookie(cookieValue).orElseThrow(SessionNotFoundException::new);
    Role userRole =
        rolesRepository
            .findByUserIdAndFileId(session.getUser().getId(), fileId)
            .orElseThrow(NoRoleFoundException::new);
    if (userRole.getType() != OWNER) {
      throw new AuthorizationDeniedException("You don't have permission to move file");
    }
    return ResponseEntity.ok(filesService.moveFile(fileId, folderId));
  }

  @Override
  public ResponseEntity<FileInfoResponse> uploadFileToFolder(Long folderId, Part file)
      throws NoSuchDirectoryException,
          BucketNotFoundException,
          ServerException,
          InsufficientDataException,
          ErrorResponseException,
          IOException,
          NoSuchAlgorithmException,
          InvalidKeyException,
          InvalidResponseException,
          XmlParserException,
          InternalException {
    return ResponseEntity.ok(filesService.uploadFileToFolder(folderId, file));
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
