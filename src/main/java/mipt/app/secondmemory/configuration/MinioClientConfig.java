package mipt.app.secondmemory.configuration;

import io.minio.MinioClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MinioClientConfig {

  @Bean
  public MinioClient createMinioClient(
      @Value("${mipt.app.minio.access-key}") String accessKey,
      @Value("${mipt.app.minio.secret-key}") String secretKey,
      @Value("${mipt.app.minio.endpoint}") String endpoint) {
    return MinioClient.builder().endpoint(endpoint).credentials(accessKey, secretKey).build();
  }
}
