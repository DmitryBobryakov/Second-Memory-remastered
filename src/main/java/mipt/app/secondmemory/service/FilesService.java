package mipt.app.secondmemory.service;

import io.minio.Result;
import io.minio.messages.Item;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mipt.app.secondmemory.dto.DirectoryInfoRequest;
import mipt.app.secondmemory.dto.FileInfoRequest;
import mipt.app.secondmemory.dto.FileInfoResponse;
import mipt.app.secondmemory.dto.RootDirectoriesRequest;
import mipt.app.secondmemory.entity.FileEntity;
import mipt.app.secondmemory.exception.DatabaseException;
import mipt.app.secondmemory.exception.NoSuchBucketException;
import mipt.app.secondmemory.exception.NoSuchDirectoryException;
import mipt.app.secondmemory.exception.NoSuchFileException;
import mipt.app.secondmemory.repository.DirectoriesRepository;
import mipt.app.secondmemory.repository.FilesRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class FilesService {
  private final FilesRepository filesRepository;
  private final DirectoriesRepository directoriesRepository;

  @Cacheable(cacheNames = {"receivedFileInfo"}, key = "{#fileId}")
  public FileInfoResponse getFileInfo(long fileId, FileInfoRequest fileInfoRequest) throws NoSuchFileException, DatabaseException {
    log.info("File ID: {}, User ID: {}", fileId, fileInfoRequest.userId());

    Optional<FileEntity> result = filesRepository.findById(fileId);
    if (result.isPresent()) {
      FileEntity file = result.get();
      return new FileInfoResponse(file.getId(), file.getName(), file.getOwnerId(), file.getCreationDate(), file.getLastModifiedDate(), file.getAccessLevelId());
    } else {
      throw new DatabaseException("Cannot select file data from DB");
    }
  }

  @Cacheable(cacheNames = {"receivedFilesInDirectory"}, key = "{#directoryInfoRequest.pathToDirectory()}")
  public Iterable<Result<Item>> getFilesInDirectory(DirectoryInfoRequest directoryInfoRequest) throws NoSuchDirectoryException {
    log.info("Bucket name: {}, Path to directory: {}, User ID: {}", directoryInfoRequest.bucketName(), directoryInfoRequest.pathToDirectory(), directoryInfoRequest.userId());

    return directoriesRepository.getFilesInDirectory(directoryInfoRequest);
  }

  @Cacheable(cacheNames = {"receivedRootDirectories"}, key = "{#rootDirectoriesRequest.bucketName()}")
  public Iterable<Result<Item>> getRootDirectories(RootDirectoriesRequest rootDirectoriesRequest) throws NoSuchBucketException {
    log.info("Bucket name: {}, User ID: {}", rootDirectoriesRequest.bucketName(), rootDirectoriesRequest.userId());

    return directoriesRepository.getRootDirectories(rootDirectoriesRequest);
  }
}
