package mipt.app.secondmemory.repository;

import io.minio.ListObjectsArgs;
import io.minio.Result;
import io.minio.messages.Item;
import mipt.app.secondmemory.configuration.MinioConfig;
import mipt.app.secondmemory.dto.DirectoryInfoRequest;
import mipt.app.secondmemory.dto.RootDirectoriesRequest;
import org.springframework.stereotype.Repository;

@Repository
public class DirectoriesRepository {

  private static final MinioConfig minioConfig = new MinioConfig();

  public Iterable<Result<Item>> getFilesInDirectory(DirectoryInfoRequest directoryInfoRequest) {
    return minioConfig.getMinioClient().listObjects(
        ListObjectsArgs.builder()
            .bucket(directoryInfoRequest.bucketName())
            .prefix(directoryInfoRequest.pathToDirectory())
            .build());
  }

  public Iterable<Result<Item>> getRootDirectories(RootDirectoriesRequest rootDirectoriesRequest) {
    return minioConfig.getMinioClient().listObjects(
        ListObjectsArgs.builder()
            .bucket(rootDirectoriesRequest.bucketName())
            .prefix("/")
            .build());
  }
}