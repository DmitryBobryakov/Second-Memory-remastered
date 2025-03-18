package mipt.app.secondmemory.controller;

import io.minio.Result;
import io.minio.messages.Item;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import mipt.app.secondmemory.dto.DirectoryInfoRequest;
import mipt.app.secondmemory.dto.FileInfoRequest;
import mipt.app.secondmemory.dto.FileInfoResponse;
import mipt.app.secondmemory.dto.RootDirectoriesRequest;
import mipt.app.secondmemory.exception.DatabaseException;
import mipt.app.secondmemory.exception.NoSuchBucketException;
import mipt.app.secondmemory.exception.NoSuchDirectoryException;
import mipt.app.secondmemory.exception.NoSuchFileException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "Files API", description = "Управление файлами")
public interface FilesOperations {

  @Operation(summary = "Получение информации о файле по ID")
  @ApiResponse(responseCode = "200", description = "Информация о файле получена")
  @GetMapping("/info/file/{fileId}")
  ResponseEntity<FileInfoResponse> getFileInfo(@Parameter(description = "ID файла") @PathVariable("fileId") long fileId, @Parameter(description = "ID пользователя") @RequestBody
      FileInfoRequest fileInfoRequest) throws NoSuchFileException, DatabaseException;

  @Operation(summary = "Получение информации о файлах в папке по пути до папки и названию бакета")
  @ApiResponse(responseCode = "200", description = "Информация о файлах в папке получена")
  @GetMapping("/info/directory")
  ResponseEntity<Iterable<Result<Item>>> getFilesInDirectory(@Parameter(description = "ID пользователя, путь до папки и название бакета") @RequestBody DirectoryInfoRequest directoryInfoRequest) throws NoSuchDirectoryException;

  @Operation(summary = "Получение корневых папок бакета по его названию")
  @ApiResponse(responseCode = "200", description = "Корневые папки бакета получены")
  @GetMapping("/root/directories")
  ResponseEntity<Iterable<Result<Item>>> getRootDirectories(@Parameter(description = "Название бакета и ID пользователя") @RequestBody RootDirectoriesRequest rootDirectoriesRequest) throws NoSuchBucketException;
}
