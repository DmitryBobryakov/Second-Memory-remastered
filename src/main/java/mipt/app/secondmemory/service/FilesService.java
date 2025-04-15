package mipt.app.secondmemory.service;

import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.Result;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mipt.app.secondmemory.dto.directory.DirectoryInfoRequest;
import mipt.app.secondmemory.dto.directory.RootDirectoriesRequest;
import mipt.app.secondmemory.dto.file.FileInfoRequest;
import mipt.app.secondmemory.dto.file.FileInfoResponse;
import mipt.app.secondmemory.entity.FileEntity;
import mipt.app.secondmemory.exception.directory.NoSuchBucketException;
import mipt.app.secondmemory.exception.directory.NoSuchDirectoryException;
import mipt.app.secondmemory.exception.file.DatabaseException;
import mipt.app.secondmemory.exception.file.FileMemoryOverflowException;
import mipt.app.secondmemory.exception.file.FileNotFoundException;
import mipt.app.secondmemory.mapper.FileMapper;
import mipt.app.secondmemory.repository.DirectoriesRepository;
import mipt.app.secondmemory.repository.FilesRepository;
import mipt.app.secondmemory.repository.FilesS3RepositoryImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

@Service
@Slf4j
@RequiredArgsConstructor
@Component
public class FilesService {
  @Value("${mipt.app.servlet.multipart.max-capacity}")
  private long maxFileSize;

  private final FilesS3RepositoryImpl filesS3Repository;
  private final MinioClient client;
  private final FileMapper fileMapper;
  private final FilesRepository filesRepository;
  private final DirectoriesRepository directoriesRepository;

  public ModelAndView download(String bucketName, String key)
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
    log.info("Функция по скачиванию файла вызвана в сервисе");
    boolean found = client.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
    if (!found) {
      throw new FileNotFoundException();
    }
    return filesS3Repository.download(bucketName, key);
  }

  @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
  public void uploadSingle(String bucketName, MultipartFile file)
      throws ServerException,
          InsufficientDataException,
          ErrorResponseException,
          IOException,
          NoSuchAlgorithmException,
          InvalidKeyException,
          InvalidResponseException,
          XmlParserException,
          InternalException,
          FileMemoryOverflowException {
    log.info("Функция по загрузке файла вызвана в сервисе");
    boolean found = client.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
    if (!found) {
      client.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
    }
    if (file.getSize() > maxFileSize) {
      throw new FileMemoryOverflowException();
    }
    filesS3Repository.upload(bucketName, file);
  }

  @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.REPEATABLE_READ)
  public void rename(String bucketName, String oldKey, String newKey)
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
    log.info("Функция по переименованию файла вызвана в сервисе");
    if (oldKey.equals(newKey)) {
      return;
    }
    boolean found = client.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
    if (!found) {
      throw new FileNotFoundException();
    }
    filesS3Repository.rename(bucketName, oldKey, newKey);
  }

  @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.REPEATABLE_READ)
  public void delete(String bucketName, String key)
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
    log.info("Функция по удалению файла вызвана в сервисе");
    boolean found = client.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
    if (!found) {
      throw new FileNotFoundException();
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
    log.info("Функция по перемещению файла внутри бакета вызвана в репозитории");
    boolean found = client.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
    if (!found) {
      throw new FileNotFoundException();
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
          FileNotFoundException {
    log.info("Функция по перемещению файла через бакеты вызвана в репозитории");
    boolean foundInOldBucket =
        client.bucketExists(BucketExistsArgs.builder().bucket(oldBucketName).build());
    boolean foundInNewBucket =
        client.bucketExists(BucketExistsArgs.builder().bucket(newBucketName).build());
    if (!foundInNewBucket || !foundInOldBucket) {
      throw new FileNotFoundException();
    }
    filesS3Repository.moveBetweenBuckets(oldBucketName, newBucketName, key);
  }

  @Cacheable(
      cacheNames = {"receivedFileInfo"},
      key = "{#fileId}")
  public FileInfoResponse getFileInfo(long fileId, FileInfoRequest fileInfoRequest)
      throws FileNotFoundException, DatabaseException {
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
