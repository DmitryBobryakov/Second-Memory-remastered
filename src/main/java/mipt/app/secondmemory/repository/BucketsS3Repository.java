package mipt.app.secondmemory.repository;

import io.minio.BucketExistsArgs;
import io.minio.ListObjectsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.RemoveBucketArgs;
import io.minio.Result;
import io.minio.errors.ErrorResponseException;
import io.minio.errors.InsufficientDataException;
import io.minio.errors.InternalException;
import io.minio.errors.InvalidResponseException;
import io.minio.errors.ServerException;
import io.minio.errors.XmlParserException;
import io.minio.messages.Item;
import lombok.RequiredArgsConstructor;
import mipt.app.secondmemory.exception.file.FileNotFoundException;
import mipt.app.secondmemory.service.FilesService;
import org.springframework.stereotype.Repository;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

@Repository
@RequiredArgsConstructor
public class BucketsS3Repository {
  private final MinioClient client;
  private final FilesService filesService;

  public void createBucket(String bucketName)
      throws ServerException,
          InsufficientDataException,
          ErrorResponseException,
          IOException,
          NoSuchAlgorithmException,
          InvalidKeyException,
          InvalidResponseException,
          XmlParserException,
          InternalException {
    boolean found = client.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
    if (!found) {
      client.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
    }
  }

  public void deleteBucket(String bucketName)
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
    // remove the contents from the bucket. MinIO
    ArrayList<String> filesNames = new ArrayList<>();
    for (Result<Item> result :
        client.listObjects(ListObjectsArgs.builder().bucket(bucketName).recursive(true).build())) {
      filesNames.add(result.get().objectName());
    }
    for (String fileName : filesNames) {
      filesService.delete(bucketName, fileName);
    }
    // remove bucket from MinIO
    client.removeBucket(RemoveBucketArgs.builder().bucket(bucketName).build());
  }
}
