package mipt.app.secondmemory.controller;

import io.minio.Result;
import io.minio.messages.Item;
import lombok.RequiredArgsConstructor;
import mipt.app.secondmemory.dto.DirectoryInfoRequest;
import mipt.app.secondmemory.dto.FileInfoRequest;
import mipt.app.secondmemory.dto.FileInfoResponse;
import mipt.app.secondmemory.dto.RootDirectoriesRequest;
import mipt.app.secondmemory.exception.DatabaseException;
import mipt.app.secondmemory.exception.NoSuchBucketException;
import mipt.app.secondmemory.exception.NoSuchDirectoryException;
import mipt.app.secondmemory.exception.NoSuchFileException;
import mipt.app.secondmemory.service.FilesService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class FilesController implements FilesOperations {
  private final FilesService filesService;

  @Override
  public ResponseEntity<FileInfoResponse> getFileInfo(long fileId, FileInfoRequest fileInfoRequest)
      throws NoSuchFileException, DatabaseException {
    return ResponseEntity.ok(filesService.getFileInfo(fileId, fileInfoRequest));
  }

  @Override
  public ResponseEntity<Iterable<Result<Item>>> getFilesInDirectory(DirectoryInfoRequest directoryInfoRequest) throws NoSuchDirectoryException {
    return ResponseEntity.ok(filesService.getFilesInDirectory(directoryInfoRequest));
  }

  @Override
  public ResponseEntity<Iterable<Result<Item>>> getRootDirectories(RootDirectoriesRequest rootDirectoriesRequest) throws NoSuchBucketException {
    return ResponseEntity.ok(filesService.getRootDirectories(rootDirectoriesRequest));
  }
}