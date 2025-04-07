package mipt.app.secondmemory.service.file;

import io.minio.Result;
import io.minio.messages.Item;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mipt.app.secondmemory.dto.directory.DirectoryInfoRequest;
import mipt.app.secondmemory.dto.file.FileInfoRequest;
import mipt.app.secondmemory.dto.file.FileInfoResponse;
import mipt.app.secondmemory.dto.directory.RootDirectoriesRequest;
import mipt.app.secondmemory.entity.FileEntity;
import mipt.app.secondmemory.exception.file.DatabaseException;
import mipt.app.secondmemory.exception.directory.NoSuchBucketException;
import mipt.app.secondmemory.exception.directory.NoSuchDirectoryException;
import mipt.app.secondmemory.exception.file.NoSuchFileException;
import mipt.app.secondmemory.mapper.FileMapper;
import mipt.app.secondmemory.repository.directory.DirectoriesRepository;
import mipt.app.secondmemory.repository.file.FilesRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class FilesService {

  private final FileMapper fileMapper;
  private final FilesRepository filesRepository;
  private final DirectoriesRepository directoriesRepository;

  @Cacheable(
      cacheNames = {"receivedFileInfo"},
      key = "{#fileId}")
  public FileInfoResponse getFileInfo(long fileId, FileInfoRequest fileInfoRequest)
      throws NoSuchFileException, DatabaseException {
    log.info("File ID: {}, User ID: {}", fileId, fileInfoRequest.userId());

    Optional<FileEntity> result = filesRepository.findById(fileId);
    if (result.isPresent()) {
      FileEntity file = result.get();
      return new FileInfoResponse(
          file.getId(),
          file.getName(),
          file.getCapacity(),
          file.getOwnerId(),
          file.getCreationDate(),
          file.getLastModifiedDate(),
          file.getBucketId());
    } else {
      throw new DatabaseException("Cannot select file data from DB");
    }
  }

  public List<FileInfoResponse> searchFiles(String name) {
    List<FileEntity> files = filesRepository.findByNameLike(name);
    List<FileInfoResponse> resultFiles = new ArrayList<>();
    for (FileEntity file : files) {
      resultFiles.add(fileMapper.toDto(file));
    }
    return resultFiles;
  }

  @Cacheable(
      cacheNames = {"receivedFilesInDirectory"},
      key = "{#directoryInfoRequest.pathToDirectory()}")
  public Iterable<Result<Item>> getFilesInDirectory(DirectoryInfoRequest directoryInfoRequest)
      throws NoSuchDirectoryException {
    log.info(
        "Bucket name: {}, Path to directory: {}, User ID: {}",
        directoryInfoRequest.bucketName(),
        directoryInfoRequest.pathToDirectory(),
        directoryInfoRequest.userId());

    return directoriesRepository.getFilesInDirectory(directoryInfoRequest);
  }

  @Cacheable(
      cacheNames = {"receivedRootDirectories"},
      key = "{#rootDirectoriesRequest.bucketName()}")
  public Iterable<Result<Item>> getRootDirectories(RootDirectoriesRequest rootDirectoriesRequest)
      throws NoSuchBucketException {
    log.info(
        "Bucket name: {}, User ID: {}",
        rootDirectoriesRequest.bucketName(),
        rootDirectoriesRequest.userId());

    return directoriesRepository.getRootDirectories(rootDirectoriesRequest);
  }
}
