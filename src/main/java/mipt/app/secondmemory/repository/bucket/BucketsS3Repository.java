package mipt.app.secondmemory.repository.bucket;

import io.minio.BucketExistsArgs;
import io.minio.ListObjectsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.RemoveBucketArgs;
import io.minio.RemoveObjectArgs;
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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class BucketsS3Repository {
  private final MinioClient client;

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

  public void deleteBucket(String bucketName, String folderPrefix)
      throws ServerException,
          InsufficientDataException,
          ErrorResponseException,
          IOException,
          NoSuchAlgorithmException,
          InvalidKeyException,
          InvalidResponseException,
          XmlParserException,
          InternalException {
    // remove the contents from the bucket. MinIO
    ArrayList<String> filesNames = new ArrayList<>();
    for (Result<Item> result :
        client.listObjects(ListObjectsArgs.builder().bucket(bucketName).recursive(true).build())) {
      if (result.get().objectName().equals(folderPrefix)) {
        continue;
      }
      filesNames.add(result.get().objectName());
    }
    for (String fileName : filesNames) {

      client.removeObject(RemoveObjectArgs.builder().bucket(bucketName).object(fileName).build());
    }
    // remove bucket from MinIO
    if (folderPrefix.equals("/")) {
      client.removeBucket(RemoveBucketArgs.builder().bucket(bucketName).build());
    } else {
      client.removeObject(
          RemoveObjectArgs.builder().bucket(bucketName).object(folderPrefix).build());
    }
  }
}
