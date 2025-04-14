package mipt.app.secondmemory.configuration;

import io.minio.MinioClient;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MinioClientConfig {
  @Value("${mipt.app.minio.access-key}")
  private String accessKey;

  @Value("${mipt.app.minio.secret-key}")
  private String secretKey;

  @Value("${mipt.app.minio.endpoint}")
  private String endpoint;

  private MinioClient client;

  @PostConstruct
  private void createMinioClient() {
    client = MinioClient.builder().endpoint(endpoint).credentials(accessKey, secretKey).build();
  }

  @Bean
  public MinioClient getClient() {
    return client;
  }
}
