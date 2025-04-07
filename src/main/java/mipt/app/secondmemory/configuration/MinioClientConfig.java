package mipt.app.secondmemory.configuration;

import io.minio.MinioClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MinioClientConfig {
  private static String accessKey;
  private static String secretKey;
  private static String endpoint;

  @Value("${mipt.app.minio.access-key}")
  public void setAccessKey(String accessKey) {
    MinioClientConfig.accessKey = accessKey;
  }

  @Value("${mipt.app.minio.secret-key}")
  public void setSecretKey(String secretKey) {
    MinioClientConfig.secretKey = secretKey;
  }

  @Value("${mipt.app.minio.endpoint}")
  public void setEndpoint(String endpoint) {
    MinioClientConfig.endpoint = endpoint;
  }

  @Bean
  public static MinioClient createMinioClient() {
    return MinioClient.builder().endpoint(endpoint).credentials(accessKey, secretKey).build();
  }
}
