package mipt.app.secondmemory.service;

import io.minio.MinioClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mipt.app.secondmemory.configuration.MinioClientConfig;
import mipt.app.secondmemory.dto.directory.BucketDto;
import mipt.app.secondmemory.entity.BucketEntity;
import mipt.app.secondmemory.exception.directory.BucketNotFoundException;
import mipt.app.secondmemory.mapper.BucketMapper;
import mipt.app.secondmemory.repository.BucketsJpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class BucketService {
  private final BucketsJpaRepository bucketsJpaRepository;
  private final BucketMapper bucketMapper;
  private final MinioClient client = new MinioClientConfig().getClient();

  public BucketDto get(Long bucketId) throws BucketNotFoundException {
    log.info("Функция по взятию тега вызвана в сервисе");
    BucketEntity bucketEntity =
        bucketsJpaRepository.findById(bucketId).orElseThrow(BucketNotFoundException::new);
    return bucketMapper.toDto(bucketEntity);
  }

  @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
  public BucketDto create(BucketEntity bucketEntity) {
    log.info("Функция по созданию тега вызвана в сервисе");
    bucketsJpaRepository.save(bucketEntity);
    return bucketMapper.toDto(bucketEntity);
  }

  @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.REPEATABLE_READ)
  public BucketDto delete(Long bucketId) throws BucketNotFoundException {
    log.info("Функция по удалению тега вызвана в сервисе");
    BucketEntity bucketEntity =
        bucketsJpaRepository.findById(bucketId).orElseThrow(BucketNotFoundException::new);
    bucketsJpaRepository.deleteById(bucketId);
    return bucketMapper.toDto(bucketEntity);
  }
}
