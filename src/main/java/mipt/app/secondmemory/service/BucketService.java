package mipt.app.secondmemory.service;

import io.minio.errors.ErrorResponseException;
import io.minio.errors.InsufficientDataException;
import io.minio.errors.InternalException;
import io.minio.errors.InvalidResponseException;
import io.minio.errors.ServerException;
import io.minio.errors.XmlParserException;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mipt.app.secondmemory.dto.directory.BucketDto;
import mipt.app.secondmemory.entity.BucketEntity;
import mipt.app.secondmemory.entity.FolderEntity;
import mipt.app.secondmemory.exception.directory.BucketNotFoundException;
import mipt.app.secondmemory.mapper.BucketMapper;
import mipt.app.secondmemory.repository.bucket.BucketsJpaRepository;
import mipt.app.secondmemory.repository.bucket.BucketsS3Repository;
import mipt.app.secondmemory.repository.folder.FoldersJpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class BucketService {
  private final BucketsJpaRepository bucketsJpaRepository;
  private final BucketsS3Repository bucketsS3Repository;
  private final FoldersJpaRepository foldersJpaRepository;

  public BucketDto getBucket(Long bucketId) throws BucketNotFoundException {
    log.debug("Функция по взятию bucket with bucketId: {} вызвана в сервисе", bucketId);
    BucketEntity bucketEntity =
        bucketsJpaRepository.findById(bucketId).orElseThrow(BucketNotFoundException::new);
    return BucketMapper.toBucketDto(bucketEntity);
  }

  @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
  public BucketDto createBucket(String bucketName)
      throws ServerException,
          InsufficientDataException,
          ErrorResponseException,
          IOException,
          NoSuchAlgorithmException,
          InvalidKeyException,
          InvalidResponseException,
          XmlParserException,
          InternalException {
    log.debug("Функция по созданию bucket with bucketName: {} вызвана в сервисе", bucketName);
    BucketEntity bucketEntity = BucketEntity.builder().name(bucketName).build();
    bucketsJpaRepository.save(bucketEntity);
    FolderEntity folderEntity =
        FolderEntity.builder().name("").bucketId(bucketEntity.getId()).parentId(null).build();
    foldersJpaRepository.save(folderEntity);
    bucketEntity.setRootFolderId(folderEntity.getId());
    bucketsJpaRepository.save(bucketEntity);
    bucketsS3Repository.createBucket(bucketEntity.getName());
    return BucketMapper.toBucketDto(bucketEntity);
  }

  @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
  public BucketDto deleteBucket(Long bucketId, String folderPrefix)
      throws BucketNotFoundException,
          ServerException,
          InsufficientDataException,
          ErrorResponseException,
          IOException,
          NoSuchAlgorithmException,
          InvalidKeyException,
          InvalidResponseException,
          XmlParserException,
          InternalException {
    log.debug("Функция по удалению  bucket with bucketId: {}  вызвана в сервисе", bucketId);
    BucketEntity bucketEntity =
        bucketsJpaRepository.findById(bucketId).orElseThrow(BucketNotFoundException::new);
    String bucketName = bucketEntity.getName();
    bucketsJpaRepository.deleteById(bucketId);
    bucketsS3Repository.deleteBucket(bucketName, folderPrefix);
    return BucketMapper.toBucketDto(bucketEntity);
  }

  @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
  public List<String> getAllBucketsNames() {
    log.debug("Функция по взятию всех файлов вызвана в сервисе");
    return bucketsJpaRepository.findAll().stream().map(BucketMapper::toBucketName).toList();
  }
}
