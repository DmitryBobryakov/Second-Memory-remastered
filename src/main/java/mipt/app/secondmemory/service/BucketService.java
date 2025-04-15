package mipt.app.secondmemory.service;

import io.minio.BucketExistsArgs;
import io.minio.ListObjectsArgs;
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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mipt.app.secondmemory.dto.directory.BucketDto;
import mipt.app.secondmemory.entity.BucketEntity;
import mipt.app.secondmemory.exception.directory.BucketNotFoundException;
import mipt.app.secondmemory.exception.file.FileNotFoundException;
import mipt.app.secondmemory.mapper.BucketMapper;
import mipt.app.secondmemory.repository.BucketsJpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class BucketService {
  private final BucketsJpaRepository bucketsJpaRepository;
  private final BucketMapper bucketMapper;
  private final MinioClient client;
  private final FilesService filesService;

  public BucketDto getBucket(Long bucketId) throws BucketNotFoundException {
    log.debug("Функция по взятию bucket with bucketId: {} вызвана в сервисе", bucketId);
    BucketEntity bucketEntity =
        bucketsJpaRepository.findById(bucketId).orElseThrow(BucketNotFoundException::new);
    return bucketMapper.toDto(bucketEntity);
  }

  @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
  public BucketDto createBucket(BucketEntity bucketEntity)
      throws ServerException,
          InsufficientDataException,
          ErrorResponseException,
          IOException,
          NoSuchAlgorithmException,
          InvalidKeyException,
          InvalidResponseException,
          XmlParserException,
          InternalException {
    log.debug(
        "Функция по созданию bucket with bucketName: {} вызвана в сервисе", bucketEntity.getName());
    bucketsJpaRepository.save(bucketEntity);
    boolean found =
        client.bucketExists(BucketExistsArgs.builder().bucket(bucketEntity.getName()).build());
    if (!found) {
      client.makeBucket(MakeBucketArgs.builder().bucket(bucketEntity.getName()).build());
    }
    return bucketMapper.toDto(bucketEntity);
  }

  @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.REPEATABLE_READ)
  public BucketDto deleteBucket(Long bucketId)
      throws BucketNotFoundException,
          ServerException,
          InsufficientDataException,
          ErrorResponseException,
          IOException,
          NoSuchAlgorithmException,
          InvalidKeyException,
          InvalidResponseException,
          XmlParserException,
          InternalException,
          FileNotFoundException {
    log.debug("Функция по удалению  bucket with bucketId: {}  вызвана в сервисе", bucketId);

    // remove bucket from Postgresql
    BucketEntity bucketEntity =
        bucketsJpaRepository.findById(bucketId).orElseThrow(BucketNotFoundException::new);
    String bucketName = bucketEntity.getName();
    bucketsJpaRepository.deleteById(bucketId);

    // remove bucket from MinIO
    ArrayList<String> filesNames = new ArrayList<>();
    for (Result<Item> result :
        client.listObjects(ListObjectsArgs.builder().bucket(bucketName).build())) {
      filesNames.add(result.get().objectName());
    }
    for (String fileName : filesNames) {
      filesService.delete(bucketName, fileName);
    }

    return bucketMapper.toDto(bucketEntity);
  }

  public List<BucketDto> getAllBuckets() {
    log.debug("Функция по взятию всех файлов вызвана в сервисе");
    return bucketsJpaRepository.findBucketsNames().stream().map(bucketMapper::toDto).toList();
  }
}
