package mipt.app.secondmemory.configuration;

import io.minio.MinioClient;
import lombok.Getter;

@Getter
public class MinioConfig {
  private static final String accessKey = "miniominio";
  private static final String secretKey = "miniominio";
  private final MinioClient minioClient =
      MinioClient.builder()
          .endpoint("http://localhost:9000")
          .credentials(accessKey, secretKey)
          .build();

}