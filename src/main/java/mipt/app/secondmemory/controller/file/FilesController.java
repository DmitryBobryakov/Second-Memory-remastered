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

import jakarta.servlet.http.Part;
import mipt.app.secondmemory.dto.directory.DirectoryInfoRequest;
import mipt.app.secondmemory.dto.directory.RootDirectoriesRequest;
import mipt.app.secondmemory.dto.file.FileInfoRequest;
import mipt.app.secondmemory.dto.file.FileInfoResponse;
import mipt.app.secondmemory.exception.directory.BucketNotFoundException;
import mipt.app.secondmemory.exception.directory.NoSuchBucketException;
import mipt.app.secondmemory.exception.directory.NoSuchDirectoryException;
import mipt.app.secondmemory.exception.file.DatabaseException;
import mipt.app.secondmemory.exception.file.FileAlreadyExistsException;
import mipt.app.secondmemory.exception.file.FileMemoryLimitExceededException;
import mipt.app.secondmemory.exception.file.FileNotFoundException;
import org.springframework.http.ResponseEntity;
import jakarta.servlet.http.Part;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "File API", description = "Управление файлами")
public interface FilesController {

  @PostMapping("/files/upload/{bucketName}")
  ResponseEntity<FileInfoResponse> uploadFiles(
      @PathVariable(name = "bucketName") String bucketName, @RequestParam("file") Part file)
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
          NoSuchBucketException;

  @PatchMapping("/files/rename/{bucketName}/{oldKey}")
  ResponseEntity<Void> renameFile(
      @PathVariable(name = "bucketName") String bucketName,
      @PathVariable(name = "oldKey") String oldKey,
      @RequestParam(name = "newKey") String newKey)
      throws ServerException,
          InsufficientDataException,
          ErrorResponseException,
          IOException,
          NoSuchAlgorithmException,
          InvalidKeyException,
          InvalidResponseException,
          XmlParserException,
          InternalException,
          FileNotFoundException;

  @DeleteMapping("/files/delete/{bucketName}/{key}")
  ResponseEntity<Void> deleteFile(
      @PathVariable(name = "bucketName") String bucketName, @PathVariable(name = "key") String key)
      throws ServerException,
          InsufficientDataException,
          ErrorResponseException,
          IOException,
          NoSuchAlgorithmException,
          InvalidKeyException,
          InvalidResponseException,
          XmlParserException,
          InternalException,
          FileNotFoundException;

  @PostMapping("/files/move/{oldBucketName}/{newBucketName}")
  ResponseEntity<Void> moveFile(
      @PathVariable(name = "oldBucketName") String oldBucketName,
      @PathVariable(name = "newBucketName") String newBucketName,
      @RequestParam(name = "fileName") String fileName,
      @RequestParam(name = "oldPath") String oldPath,
      @RequestParam(name = "newPath") String newPath)
      throws ServerException,
          InsufficientDataException,
          ErrorResponseException,
          IOException,
          NoSuchAlgorithmException,
          InvalidKeyException,
          InvalidResponseException,
          XmlParserException,
          InternalException,
          FileNotFoundException,
          FileAlreadyExistsException,
          NoSuchBucketException;

  @PostMapping("/files/upload/folder/{folderId}")
  ResponseEntity<FileInfoResponse> uploadFileToFolder(
      @PathVariable(name = "folderId") Long folderId, @RequestParam("file") Part file)
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
          InternalException;

  @Operation(summary = "Получение информации о файле по ID")
  @ApiResponse(responseCode = "200", description = "Информация о файле получена")
  @GetMapping("/info/file/{fileId}")
  ResponseEntity<FileInfoResponse> getFileInfo(
      @Parameter(description = "ID файла") @PathVariable("fileId") long fileId,
      @Parameter(description = "ID пользователя") @RequestBody FileInfoRequest fileInfoRequest)
      throws FileNotFoundException, DatabaseException;

  @Operation(summary = "Получение информации о файлах в папке по пути до папки и названию бакета")
  @ApiResponse(responseCode = "200", description = "Информация о файлах в папке получена")
  @GetMapping("/info/directory")
  ResponseEntity<Iterable<Result<Item>>> getFilesInDirectory(
      @Parameter(description = "ID пользователя, путь до папки и название бакета") @RequestBody
          DirectoryInfoRequest directoryInfoRequest)
      throws NoSuchDirectoryException;

  @Operation(summary = "Получение корневых папок бакета по его названию")
  @ApiResponse(responseCode = "200", description = "Корневые папки бакета получены")
  @GetMapping("/root/directories")
  ResponseEntity<Iterable<Result<Item>>> getRootDirectories(
      @Parameter(description = "Название бакета и ID пользователя") @RequestBody
          RootDirectoriesRequest rootDirectoriesRequest)
      throws NoSuchBucketException;

  @PostMapping("/file-search")
  ResponseEntity<List<FileInfoResponse>> searchFilesInDirectory(@RequestBody String name);
}
