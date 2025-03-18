package mipt.app.secondmemory.configuration;

import io.minio.MinioClient;
import lombok.Getter;

public class MinioClientConfig {
    @Getter
    private static final MinioClient client;

    static {
        String accessKey = "miniominio";
        String secretKey = "miniominio";
        client =
                MinioClient.builder()
                        .endpoint("http://localhost:9000")
                        .credentials(accessKey, secretKey)
                        .build();
    }
}