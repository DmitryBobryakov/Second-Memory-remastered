package mipt.app.secondmemory.service;

import io.minio.BucketExistsArgs;
import io.minio.MinioClient;
import io.minio.Result;
import io.minio.StatObjectArgs;
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
import lombok.extern.slf4j.Slf4j;
import mipt.app.secondmemory.dto.directory.DirectoryInfoRequest;
import mipt.app.secondmemory.dto.directory.RootDirectoriesRequest;
import mipt.app.secondmemory.dto.file.FileInfoRequest;
import mipt.app.secondmemory.dto.file.FileInfoResponse;
import mipt.app.secondmemory.exception.directory.NoSuchBucketException;
import mipt.app.secondmemory.exception.directory.NoSuchDirectoryException;
import mipt.app.secondmemory.exception.file.DatabaseException;
import mipt.app.secondmemory.exception.file.FileMemoryOverflowException;
import mipt.app.secondmemory.exception.file.FileNotFoundException;
import mipt.app.secondmemory.mapper.FilesMapper;
import mipt.app.secondmemory.repository.DirectoriesRepository;
import mipt.app.secondmemory.repository.FilesRepository;
import mipt.app.secondmemory.repository.FilesS3RepositoryImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

@Service
@Slf4j
@RequiredArgsConstructor
public class FilesService {
  @Value("${mipt.app.servlet.multipart.files_max_size}")
  private long filesMaxSize;

  private final FilesS3RepositoryImpl filesS3Repository;
  private final MinioClient client;

  private final FilesRepository filesRepository;
  private final DirectoriesRepository directoriesRepository;

  public ModelAndView downloadFile(String bucketName, String key)
      throws ServerException,
          InsufficientDataException,
          ErrorResponseException,
          IOException,
          NoSuchAlgorithmException,
          InvalidKeyException,
          InvalidResponseException,
          XmlParserException,
          InternalException,
          FileNotFoundException {
    log.debug("Функция по скачиванию файла вызвана в сервисе");
    if (!checkFileExists(bucketName, key)) {
      throw new FileNotFoundException("File does not exist on the way: " + bucketName + "/" + key);
    }
    return filesS3Repository.download(bucketName, key);
  }

  @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
  public void uploadFiles(String bucketName, MultipartFile files)
      throws ServerException,
          InsufficientDataException,
          ErrorResponseException,
          IOException,
          NoSuchAlgorithmException,
          InvalidKeyException,
          InvalidResponseException,
          XmlParserException,
          InternalException,
          FileMemoryOverflowException,
          NoSuchBucketException {
    log.debug("Функция по загрузке файла вызвана в сервисе");
    boolean found = client.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
    if (!found) {
      throw new NoSuchBucketException("Bucket does not exist with name: " + bucketName);
    }
    if (files.getSize() > filesMaxSize) {
      throw new FileMemoryOverflowException("Files are too large: " + filesMaxSize);
    }
    filesS3Repository.upload(bucketName, files);
  }

  @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.REPEATABLE_READ)
  public void renameFile(String bucketName, String oldKey, String newKey)
      throws ServerException,
          InsufficientDataException,
          ErrorResponseException,
          IOException,
          NoSuchAlgorithmException,
          InvalidKeyException,
          InvalidResponseException,
          XmlParserException,
          InternalException,
          FileNotFoundException {
    log.debug("Функция по переименованию файла вызвана в сервисе");
    if (oldKey.equals(newKey)) {
      return;
    }
    if (!checkFileExists(bucketName, oldKey)) {
      throw new FileNotFoundException(
          "File does not exist on the way: " + bucketName + "/" + oldKey);
    }
    filesS3Repository.rename(bucketName, oldKey, newKey);
  }

  @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.REPEATABLE_READ)
  public void deleteFile(String bucketName, String key)
      throws ServerException,
          InsufficientDataException,
          ErrorResponseException,
          IOException,
          NoSuchAlgorithmException,
          InvalidKeyException,
          InvalidResponseException,
          XmlParserException,
          InternalException,
          FileNotFoundException {
    log.debug("Функция по удалению файла вызвана в сервисе");
    if (!checkFileExists(bucketName, key)) {
      throw new FileNotFoundException("File does not exist on the way: " + bucketName + "/" + key);
    }
    filesS3Repository.delete(bucketName, key);
  }

  public void moveInBucket(String bucketName, String fileName, String oldPath, String newPath)
      throws ServerException,
          InsufficientDataException,
          ErrorResponseException,
          IOException,
          NoSuchAlgorithmException,
          InvalidKeyException,
          InvalidResponseException,
          XmlParserException,
          InternalException,
          FileNotFoundException {
    log.debug("Функция по перемещению файла внутри бакета вызвана в репозитории");
    if (!checkFileExists(bucketName, oldPath + "/" + fileName)) {
      throw new FileNotFoundException(
          "File does not exist on the way: " + bucketName + "/" + oldPath + "/" + fileName);
    }
    filesS3Repository.moveInBucket(bucketName, fileName, oldPath, newPath);
  }

  public void moveBetweenBuckets(String oldBucketName, String newBucketName, String key)
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
          NoSuchBucketException {
    log.debug("Функция по перемещению файла через бакеты вызвана в репозитории");

    if (!checkFileExists(oldBucketName, key)) {
      throw new FileNotFoundException(
          "File does not exist on the way: " + oldBucketName + "/" + key);
    }
    boolean found = client.bucketExists(BucketExistsArgs.builder().bucket(newBucketName).build());
    if (!found) {
      throw new NoSuchBucketException("Bucket does not exist with name: " + newBucketName);
    }
    if (checkFileExists(newBucketName, key)) {
      throw new FileNotFoundException(
          "File does not exist on the way: " + newBucketName + "/" + key);
    }
    filesS3Repository.moveBetweenBuckets(oldBucketName, newBucketName, key);
  }

  @Cacheable(
      cacheNames = {"receivedFileInfo"},
      key = "{#fileId}")
  public FileInfoResponse getFileInfo(long fileId, FileInfoRequest fileInfoRequest)
      throws FileNotFoundException, DatabaseException {
    log.debug("File ID: {}, User ID: {}", fileId, fileInfoRequest.userId());
    return filesRepository
        .findById(fileId)
        .map(
            file ->
                new FileInfoResponse(
                    file.getId(),
                    file.getName(),
                    file.getCapacity(),
                    file.getOwnerId(),
                    file.getCreationDate(),
                    file.getLastModifiedDate(),
                    file.getBucketId()))
        .orElseThrow(() -> new DatabaseException("Cannot select file data from DB"));
  }

  public List<FileInfoResponse> searchFiles(String name) {
    return filesRepository.findByNameLike(name).stream().map(FilesMapper::toDto).toList();
  }

  @Cacheable(
      cacheNames = {"receivedFilesInDirectory"},
      key = "{#directoryInfoRequest.pathToDirectory()}")
  public Iterable<Result<Item>> getFilesInDirectory(DirectoryInfoRequest directoryInfoRequest)
      throws NoSuchDirectoryException {
    log.debug(
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
    log.debug(
        "Bucket name: {}, User ID: {}",
        rootDirectoriesRequest.bucketName(),
        rootDirectoriesRequest.userId());

    return directoriesRepository.getRootDirectories(rootDirectoriesRequest);
  }

  private boolean checkFileExists(String bucketName, String key)
      throws ServerException,
          InsufficientDataException,
          IOException,
          NoSuchAlgorithmException,
          InvalidKeyException,
          InvalidResponseException,
          XmlParserException,
          InternalException,
          ErrorResponseException {
    boolean flag = false;
    try {
      client.statObject(StatObjectArgs.builder().bucket(bucketName).object(key).build());
      flag = true;
    } catch (ErrorResponseException e) {
      if (!e.errorResponse().code().equals("NoSuchKey")) {
        throw e;
      }
    }
    return flag;
  }
}
