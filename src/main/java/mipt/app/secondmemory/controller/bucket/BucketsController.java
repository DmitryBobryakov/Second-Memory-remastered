package mipt.app.secondmemory.controller.bucket;

import io.minio.errors.ErrorResponseException;
import io.minio.errors.InsufficientDataException;
import io.minio.errors.InternalException;
import io.minio.errors.InvalidResponseException;
import io.minio.errors.ServerException;
import io.minio.errors.XmlParserException;
import io.swagger.v3.oas.annotations.tags.Tag;
import mipt.app.secondmemory.dto.directory.BucketDto;
import mipt.app.secondmemory.entity.BucketEntity;
import mipt.app.secondmemory.exception.directory.BucketNotFoundException;
import mipt.app.secondmemory.exception.file.FileNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

@Tag(name = "Bucket API", description = "Управление корневыми папками")
public interface BucketsController {

  @GetMapping("/buckets/get/{bucketId}")
  ResponseEntity<BucketDto> getBucket(@PathVariable(value = "bucketId") Long bucketId)
      throws BucketNotFoundException;

  @PostMapping("/buckets/create")
  ResponseEntity<BucketDto> createBucket(@RequestBody BucketEntity bucketEntity)
      throws ServerException,
          InsufficientDataException,
          ErrorResponseException,
          IOException,
          NoSuchAlgorithmException,
          InvalidKeyException,
          InvalidResponseException,
          XmlParserException,
          InternalException;

  @DeleteMapping("/buckets/delete/{bucketId}")
  ResponseEntity<BucketDto> deleteBucket(
      @PathVariable(value = "bucketId") Long bucketId,
      @RequestParam(name = "prefix") String folderPrefix)
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
          FileNotFoundException;

  @GetMapping("/buckets")
  ResponseEntity<List<String>> getAllBucketsNames();
}
