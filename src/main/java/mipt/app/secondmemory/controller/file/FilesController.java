package mipt.app.secondmemory.controller.file;

import io.minio.Result;
import io.minio.errors.ErrorResponseException;
import io.minio.errors.InsufficientDataException;
import io.minio.errors.InternalException;
import io.minio.errors.InvalidResponseException;
import io.minio.errors.ServerException;
import io.minio.errors.XmlParserException;
import io.minio.messages.Item;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import mipt.app.secondmemory.dto.directory.DirectoryInfoRequest;
import mipt.app.secondmemory.dto.directory.FilesAndFoldersInfoDto;
import mipt.app.secondmemory.dto.directory.RootDirectoriesRequest;
import mipt.app.secondmemory.dto.file.FileDetailsDto;
import mipt.app.secondmemory.dto.file.FileInfoRequest;
import mipt.app.secondmemory.dto.file.FileInfoResponse;
import mipt.app.secondmemory.exception.directory.BucketNotFoundException;
import mipt.app.secondmemory.exception.directory.NoSuchBucketException;
import mipt.app.secondmemory.exception.directory.NoSuchDirectoryException;
import mipt.app.secondmemory.exception.file.FileAlreadyExistsException;
import mipt.app.secondmemory.exception.file.FileMemoryLimitExceededException;
import mipt.app.secondmemory.exception.file.FileNotFoundException;
import mipt.app.secondmemory.exception.session.SessionNotFoundException;
import mipt.app.secondmemory.exception.user.UserNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "File API", description = "Управление файлами")
public interface FilesController {

  @PostMapping("/files/upload/{bucketId}")
  ResponseEntity<FileInfoResponse> uploadFile(
      @PathVariable(name = "bucketId") Long bucketId,
      @RequestBody MultipartFile file,
      @CookieValue("token") String cookieValue)
      throws ServerException,
          InsufficientDataException,
          ErrorResponseException,
          IOException,
          NoSuchAlgorithmException,
          InvalidKeyException,
          InvalidResponseException,
          XmlParserException,
          InternalException,
          FileMemoryLimitExceededException,
          SessionNotFoundException,
          NoSuchBucketException,
          BucketNotFoundException;

  @PatchMapping("/files/rename/{fileId}")
  ResponseEntity<FileInfoResponse> renameFile(
      @PathVariable(name = "fileId") Long fileId,
      @RequestParam(name = "newFileName") String newFileName,
      @CookieValue("token") String cookieValue)
      throws ServerException,
          InsufficientDataException,
          ErrorResponseException,
          IOException,
          NoSuchAlgorithmException,
          InvalidKeyException,
          InvalidResponseException,
          XmlParserException,
          InternalException,
          NoSuchDirectoryException,
          BucketNotFoundException,
          FileNotFoundException,
          SessionNotFoundException;

  @DeleteMapping("/files/delete")
  ResponseEntity<FileInfoResponse> deleteFile(
      @RequestParam(name = "fileId") Long fileId, @CookieValue("token") String cookieValue)
      throws ServerException,
          InsufficientDataException,
          ErrorResponseException,
          IOException,
          NoSuchAlgorithmException,
          InvalidKeyException,
          InvalidResponseException,
          XmlParserException,
          InternalException,
          NoSuchDirectoryException,
          BucketNotFoundException,
          FileNotFoundException,
          SessionNotFoundException;

  @PostMapping("/files/move/")
  ResponseEntity<FileInfoResponse> moveFile(
      @RequestParam(name = "fileId") Long fileId,
      @RequestParam(name = "folderId") Long folderId,
      @CookieValue("token") String cookieValue)
      throws ServerException,
          InsufficientDataException,
          ErrorResponseException,
          IOException,
          NoSuchAlgorithmException,
          InvalidKeyException,
          InvalidResponseException,
          XmlParserException,
          InternalException,
          FileAlreadyExistsException,
          NoSuchDirectoryException,
          BucketNotFoundException,
          NoSuchBucketException,
          SessionNotFoundException,
          FileNotFoundException;

  @PostMapping("/files/upload/folder/{folderId}")
  ResponseEntity<FileInfoResponse> uploadFileToFolder(
      @PathVariable(name = "folderId") Long folderId,
      @RequestParam("file") MultipartFile file,
      @CookieValue("token") String cookieValue)
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
          InternalException,
          SessionNotFoundException;

  @Operation(summary = "Получение информации о файле по ID")
  @ApiResponse(responseCode = "200", description = "Информация о файле получена")
  @PostMapping("/info/file/{fileId}")
  ResponseEntity<FileDetailsDto> getFileInfo(
      @Parameter(description = "ID файла") @PathVariable("fileId") Long fileId,
      @CookieValue("token") String cookieValue)
      throws FileNotFoundException, SessionNotFoundException, BucketNotFoundException, UserNotFoundException;

  @Operation(summary = "Получение информации о файлах в папке по пути до папки и названию бакета")
  @ApiResponse(responseCode = "200", description = "Информация о файлах в папке получена")
  @GetMapping("/info/directory")
  ResponseEntity<Iterable<Result<Item>>> getFilesInDirectory(
      @Parameter(description = "ID пользователя, путь до папки и название бакета") @RequestBody
          DirectoryInfoRequest directoryInfoRequest,
      @CookieValue("token") String cookieValue)
      throws NoSuchDirectoryException;

  @Operation(summary = "Получение корневых папок бакета по его названию")
  @ApiResponse(responseCode = "200", description = "Корневые папки бакета получены")
  @GetMapping("/root/directories")
  ResponseEntity<Iterable<Result<Item>>> getRootDirectories(
      @Parameter(description = "Название бакета и ID пользователя") @RequestBody
          RootDirectoriesRequest rootDirectoriesRequest,
      @CookieValue("token") String cookieValue)
      throws NoSuchBucketException;

  @PostMapping("/file-search")
  ResponseEntity<List<FileInfoResponse>> searchFilesInDirectory(@RequestBody String name);

  @PostMapping("/directory/{folderId}")
  ResponseEntity<FilesAndFoldersInfoDto> getDirectoryInfo(
      @PathVariable(name = "folderId") Long folderId);
}
