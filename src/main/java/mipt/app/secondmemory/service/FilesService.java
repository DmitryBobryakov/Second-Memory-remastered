package mipt.app.secondmemory.service;

import io.minio.Result;
import io.minio.messages.Item;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mipt.app.secondmemory.dto.DirectoryInfoRequest;
import mipt.app.secondmemory.dto.FileInfoRequest;
import mipt.app.secondmemory.dto.FileInfoResponse;
import mipt.app.secondmemory.dto.RootDirectoriesRequest;
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

    return filesRepository.findByFileId(fileId);
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
