package mipt.app.secondmemory;

import io.minio.MinioClient;
import lombok.Getter;

public class MyMinIOClient {
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