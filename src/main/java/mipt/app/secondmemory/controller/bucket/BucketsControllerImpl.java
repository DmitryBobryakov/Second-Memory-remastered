package mipt.app.secondmemory.controller.bucket;

import io.minio.errors.ErrorResponseException;
import io.minio.errors.InsufficientDataException;
import io.minio.errors.InternalException;
import io.minio.errors.InvalidResponseException;
import io.minio.errors.ServerException;
import io.minio.errors.XmlParserException;
import lombok.RequiredArgsConstructor;
import mipt.app.secondmemory.dto.directory.BucketDto;
import mipt.app.secondmemory.entity.BucketEntity;
import mipt.app.secondmemory.exception.directory.BucketNotFoundException;
import mipt.app.secondmemory.service.BucketService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/second-memory")
public class BucketsControllerImpl implements BucketsController {

  private final BucketService bucketService;

  @Override
  public ResponseEntity<BucketDto> getBucket(Long bucketId) throws BucketNotFoundException {
    return ResponseEntity.ok(bucketService.getBucket(bucketId));
  }

  @Override
  public ResponseEntity<BucketDto> createBucket(String bucketName)
      throws ServerException,
          InsufficientDataException,
          ErrorResponseException,
          IOException,
          NoSuchAlgorithmException,
          InvalidKeyException,
          InvalidResponseException,
          XmlParserException,
          InternalException {
    return ResponseEntity.ok(bucketService.createBucket(bucketName));
  }

  @Override
  public ResponseEntity<BucketDto> deleteBucket(Long bucketId, String folderPrefix)
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
    return ResponseEntity.ok(bucketService.deleteBucket(bucketId, folderPrefix));
  }

  @Override
  public ResponseEntity<List<String>> getAllBucketsNames() {
    return ResponseEntity.ok(bucketService.getAllBucketsNames());
  }
}
