package mipt.app.secondmemory.service;

import io.minio.Result;
import io.minio.messages.Item;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mipt.app.secondmemory.dto.DirectoryInfoRequest;
import mipt.app.secondmemory.dto.FileInfoRequest;
import mipt.app.secondmemory.dto.FileInfoResponse;
import mipt.app.secondmemory.dto.RootDirectoriesRequest;
import mipt.app.secondmemory.exception.NoSuchFileException;
import mipt.app.secondmemory.repository.DirectoriesRepository;
import mipt.app.secondmemory.repository.FilesRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class FilesService {
  private final FilesRepository filesRepository;
  private final DirectoriesRepository directoriesRepository;

  @Transactional(readOnly = true)
  @Cacheable(cacheNames = {"receivedFileInfo"}, key = "{#fileId}")
  public FileInfoResponse getFileInfo(long fileId, FileInfoRequest fileInfoRequest) throws NoSuchFileException {
    log.info("Функция получения информации о файле вызвана в сервисе");

    return filesRepository.findByFileId(fileId);
  }

  @Cacheable(cacheNames = {"receivedFilesInDirectory"}, key = "{#directoryInfoRequest.pathToDirectory()}")
  public Iterable<Result<Item>> getFilesInDirectory(DirectoryInfoRequest directoryInfoRequest) {
    log.info("Функция получения информации о файлах в конкретной папке вызвана в сервисе");

    return directoriesRepository.getFilesInDirectory(directoryInfoRequest);
  }

  @Cacheable(cacheNames = {"receivedRootDirectories"}, key = "{#rootDirectoriesRequest.bucketName()}")
  public Iterable<Result<Item>> getRootDirectories(RootDirectoriesRequest rootDirectoriesRequest) {
    log.info("Функция для получения папок в корневой директории бакета вызвана в сервисе");

    return directoriesRepository.getRootDirectories(rootDirectoriesRequest);
  }
}
