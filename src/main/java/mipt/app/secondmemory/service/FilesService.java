package mipt.app.secondmemory.service;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mipt.app.secondmemory.dto.directory.DirectoryInfoRequest;
import mipt.app.secondmemory.dto.directory.FilesAndFoldersInfoDto;
import mipt.app.secondmemory.dto.directory.FolderDto;
import mipt.app.secondmemory.dto.directory.RootDirectoriesRequest;
import mipt.app.secondmemory.dto.file.FileDetailsDto;
import mipt.app.secondmemory.dto.file.FileInfoResponse;
import mipt.app.secondmemory.dto.file.MessageFileDto;
import mipt.app.secondmemory.dto.tag.TagDto;
import mipt.app.secondmemory.entity.BucketEntity;
import mipt.app.secondmemory.entity.FileEntity;
import mipt.app.secondmemory.entity.FolderEntity;
import mipt.app.secondmemory.entity.Role;
import mipt.app.secondmemory.entity.RoleType;
import mipt.app.secondmemory.entity.User;
import mipt.app.secondmemory.exception.directory.BucketNotFoundException;
import mipt.app.secondmemory.exception.directory.NoSuchBucketException;
import mipt.app.secondmemory.exception.directory.NoSuchDirectoryException;
import mipt.app.secondmemory.exception.file.FileAlreadyExistsException;
import mipt.app.secondmemory.exception.file.FileMemoryLimitExceededException;
import mipt.app.secondmemory.exception.file.FileNotFoundException;
import mipt.app.secondmemory.exception.user.UserNotFoundException;
import mipt.app.secondmemory.mapper.FilesMapper;
import mipt.app.secondmemory.mapper.FoldersMapper;
import mipt.app.secondmemory.repository.DirectoriesRepository;
import mipt.app.secondmemory.repository.FilesRepository;
import mipt.app.secondmemory.repository.FilesS3RepositoryImpl;
import mipt.app.secondmemory.repository.RolesRepository;
import mipt.app.secondmemory.repository.UsersRepository;
import mipt.app.secondmemory.repository.bucket.BucketsJpaRepository;
import mipt.app.secondmemory.repository.folder.FoldersJpaRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
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
  private final ObjectMapper objectMapper;
  private final UsersRepository usersRepository;
  private final TagsService tagsService;

  @Value("${mipt.app.servlet.part.file-memory-limit}")
  private long fileMemoryLimit;

  private final KafkaTemplate<String, String> kafkaTemplate;
  private final FoldersJpaRepository foldersJpaRepository;
  private final BucketsJpaRepository bucketsJpaRepository;
  private final FilesS3RepositoryImpl filesS3Repository;
  private final MinioClient client;

  private final FilesRepository filesRepository;
  private final DirectoriesRepository directoriesRepository;
  private final RolesRepository rolesRepository;

  public ModelAndView downloadFile(Long fileId)
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
          BucketNotFoundException {
    log.debug("Функция по скачиванию файла вызвана в сервисе");
    FileEntity file = filesRepository.findById(fileId).orElseThrow(FileNotFoundException::new);
    String pathToFolder =
        "%s/".formatted(foldersJpaRepository.takePathToFolder(file.getFolderId()));
    String fileName = file.getName();
    String key = pathToFolder + fileName;
    String bucketName =
        bucketsJpaRepository
            .findById(file.getBucketId())
            .orElseThrow(BucketNotFoundException::new)
            .getName();
    return filesS3Repository.downloadFile(bucketName, key);
  }

  @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
  public FileInfoResponse uploadFile(Long bucketId, MultipartFile file, User user)
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
          NoSuchBucketException,
          BucketNotFoundException {
    log.debug("Функция по загрузке файла вызвана в сервисе");
    BucketEntity bucketEntity =
        bucketsJpaRepository.findById(bucketId).orElseThrow(BucketNotFoundException::new);
    String bucketName = bucketEntity.getName();
    boolean found = client.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
    if (!found) {
      throw new NoSuchBucketException("Bucket does not exist with name: " + bucketName);
    }
    if (file.getSize() > fileMemoryLimit) {
      throw new FileMemoryLimitExceededException("Files are too large: " + fileMemoryLimit);
    }
    filesS3Repository.uploadFile(bucketName, file);
    FileEntity fileEntity =
        FilesMapper.toFileEntity(file, user.getId(), bucketId, bucketEntity.getRootFolderId());
    filesRepository.save(fileEntity);
    Role ownerRole = new Role(user, fileEntity, RoleType.OWNER);
    rolesRepository.save(ownerRole);
    kafkaTemplate.send(
        "files_topic",
        objectMapper.writeValueAsString(
            new MessageFileDto(fileEntity.getName(), fileEntity.getOwnerId(), bucketName)));
    return FilesMapper.toFileDto(fileEntity);
  }

  @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
  public FileInfoResponse renameFile(Long fileId, String newFileName)
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
          BucketNotFoundException,
          NoSuchDirectoryException {
    log.debug("Функция по переименованию файла вызвана в сервисе");
    FileEntity fileEntity =
        filesRepository.findById(fileId).orElseThrow(FileNotFoundException::new);
    BucketEntity bucketEntity =
        bucketsJpaRepository
            .findById(fileEntity.getBucketId())
            .orElseThrow(BucketNotFoundException::new);
    FolderEntity folderEntity =
        foldersJpaRepository
            .findById(fileEntity.getFolderId())
            .orElseThrow(NoSuchDirectoryException::new);
    String pathToFolder =
        "%s/".formatted(foldersJpaRepository.takePathToFolder(folderEntity.getId()));
    String bucketName = bucketEntity.getName();
    String fileName = fileEntity.getName();
    if (fileName.equals(newFileName)) {
      return FilesMapper.toFileDto(fileEntity);
    }
    String oldKey = pathToFolder + fileName;
    String newKey = pathToFolder + newFileName;
    if (!checkFileExists(bucketName, oldKey)) {
      throw new FileNotFoundException(
          "File does not exist on the way: " + bucketName + pathToFolder);
    }
    filesS3Repository.renameFile(bucketName, oldKey, newKey);
    fileEntity.setName(newFileName);
    fileEntity.setLastModifiedTs(Timestamp.from(Instant.now()));
    filesRepository.save(fileEntity);
    return FilesMapper.toFileDto(fileEntity);
  }

  @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
  public FileInfoResponse deleteFile(Long fileId)
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
          BucketNotFoundException,
          NoSuchDirectoryException {
    log.debug("Функция по удалению файла вызвана в сервисе. FileId: {}", fileId);
    FileEntity fileEntity =
        filesRepository.findById(fileId).orElseThrow(FileNotFoundException::new);
    BucketEntity bucketEntity =
        bucketsJpaRepository
            .findById(fileEntity.getBucketId())
            .orElseThrow(BucketNotFoundException::new);
    FolderEntity folderEntity =
        foldersJpaRepository
            .findById(fileEntity.getFolderId())
            .orElseThrow(NoSuchDirectoryException::new);
    String pathToFolder =
        "%s/".formatted(foldersJpaRepository.takePathToFolder(folderEntity.getId()));
    String bucketName = bucketEntity.getName();
    String key = pathToFolder + fileEntity.getName();
    if (!checkFileExists(bucketName, key)) {
      throw new FileNotFoundException("File does not exist on the way: " + bucketName + "/" + key);
    }
    filesS3Repository.deleteFile(bucketName, key);
    filesRepository.delete(fileEntity);
    return FilesMapper.toFileDto(fileEntity);
  }

  @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
  public FileInfoResponse moveFile(Long fileId, Long folderId)
      throws FileNotFoundException,
          ServerException,
          InsufficientDataException,
          ErrorResponseException,
          IOException,
          NoSuchAlgorithmException,
          InvalidKeyException,
          InvalidResponseException,
          XmlParserException,
          InternalException,
          FileAlreadyExistsException,
          NoSuchBucketException,
          NoSuchDirectoryException,
          BucketNotFoundException {
    log.debug("Функция по перемещению файла внутри бакета вызвана в репозитории");
    FileEntity fileEntity =
        filesRepository.findById(fileId).orElseThrow(FileNotFoundException::new);
    FolderEntity currentFolderEntity =
        foldersJpaRepository
            .findById(fileEntity.getFolderId())
            .orElseThrow(NoSuchDirectoryException::new);
    FolderEntity newFolderEntity =
        foldersJpaRepository.findById(folderId).orElseThrow(NoSuchDirectoryException::new);
    BucketEntity currentBucketEntity =
        bucketsJpaRepository
            .findById(fileEntity.getBucketId())
            .orElseThrow(BucketNotFoundException::new);
    BucketEntity newBucketEntity =
        bucketsJpaRepository
            .findById(newFolderEntity.getBucketId())
            .orElseThrow(BucketNotFoundException::new);
    String oldKey =
        "%s/%s"
            .formatted(
                foldersJpaRepository.takePathToFolder(currentFolderEntity.getId()),
                fileEntity.getName());
    String newKey =
        "%s/%s".formatted(foldersJpaRepository.takePathToFolder(folderId), fileEntity.getName());
    String oldBucketName = currentBucketEntity.getName();
    String newBucketName = newBucketEntity.getName();
    String oldPath = foldersJpaRepository.takePathToFolder(currentFolderEntity.getId());
    String newPath = foldersJpaRepository.takePathToFolder(newFolderEntity.getId());
    String fileName = fileEntity.getName();
    if (!checkFileExists(oldBucketName, oldKey)) {
      throw new FileNotFoundException("File does not exist on the way: " + oldBucketName + oldKey);
    }
    boolean found = client.bucketExists(BucketExistsArgs.builder().bucket(newBucketName).build());
    if (!found) {
      throw new NoSuchBucketException("Bucket does not exist with name: " + newBucketName);
    }
    if (checkFileExists(newBucketName, newKey)) {
      throw new FileAlreadyExistsException(
          "File already exists on the way: " + newBucketName + newKey);
    }
    filesS3Repository.moveFile(oldBucketName, newBucketName, fileName, oldPath, newPath);
    fileEntity.setBucketId(newBucketEntity.getId());
    fileEntity.setFolderId(newFolderEntity.getId());
    filesRepository.save(fileEntity);
    return FilesMapper.toFileDto(fileEntity);
  }

  public FileInfoResponse uploadFileToFolder(Long folderId, MultipartFile file, User user)
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
          InternalException {
    FolderEntity folderEntity =
        foldersJpaRepository.findById(folderId).orElseThrow(NoSuchDirectoryException::new);
    String pathToFolder = foldersJpaRepository.takePathToFolder(folderId);
    String bucketName =
        bucketsJpaRepository
            .findById(folderEntity.getBucketId())
            .orElseThrow(BucketNotFoundException::new)
            .getName();
    filesS3Repository.uploadFileToFolder(bucketName, file, pathToFolder);
    FileEntity fileEntity =
        FilesMapper.toFileEntity(file, user.getId(), folderEntity.getBucketId(), folderId);
    filesRepository.save(fileEntity);
    MessageFileDto message =
        new MessageFileDto(
            foldersJpaRepository.takePathToFolder(fileEntity.getFolderId())
                + "/"
                + fileEntity.getName(),
            fileEntity.getOwnerId(),
            bucketName);
    Role ownerRole = new Role(user, fileEntity, RoleType.OWNER);
    rolesRepository.save(ownerRole);
    CompletableFuture<SendResult<String, String>> sendResult =
        kafkaTemplate.send("files_topic", objectMapper.writeValueAsString(message));
    return FilesMapper.toFileDto(fileEntity);
  }

  public List<FileInfoResponse> searchFiles(String name) {
    return filesRepository.findByNameLike(name).stream().map(FilesMapper::toFileDto).toList();
  }

  @Cacheable(
      cacheNames = {"filesInDirectory"},
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
      cacheNames = {"rootDirectories"},
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

  public FilesAndFoldersInfoDto getDirectoryInfo(Long folderId) {
    List<FileInfoResponse> files =
        filesRepository.findByFolderId(folderId).stream().map(FilesMapper::toFileDto).toList();

    List<FolderDto> folders =
        foldersJpaRepository.findByParentId(folderId).stream()
            .map(FoldersMapper::toFolderDto)
            .toList();
    return FilesAndFoldersInfoDto.builder().files(files).folders(folders).build();
  }

  public FileDetailsDto getFileDetails(Long fileId, RoleType role)
      throws FileNotFoundException, BucketNotFoundException, UserNotFoundException {
    FileEntity file = filesRepository.findById(fileId).orElseThrow(FileNotFoundException::new);
    List<TagDto> tags = tagsService.getAllTagsByFileId(fileId);
    String ownerName =
        usersRepository
            .findById(file.getOwnerId())
            .orElseThrow(UserNotFoundException::new)
            .getName();
    String bucketName =
        bucketsJpaRepository
            .findById(file.getBucketId())
            .orElseThrow(BucketNotFoundException::new)
            .getName();
    return FileDetailsDto.builder()
        .role(role.toString())
        .size(file.getCapacity())
        .tags(tags)
        .ownerName(ownerName)
        .fileName(file.getName())
        .creationTs(file.getCreationTs().getTime())
        .lastModifiedTs(file.getLastModifiedTs().getTime())
        .bucketName(bucketName)
        .build();
  }
}
