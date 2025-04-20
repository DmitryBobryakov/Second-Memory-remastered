package mipt.app.secondmemory.repository;

import io.minio.ListObjectsArgs;
import io.minio.MinioClient;
import io.minio.Result;
import io.minio.messages.Item;
import lombok.RequiredArgsConstructor;
import mipt.app.secondmemory.dto.directory.DirectoryInfoRequest;
import mipt.app.secondmemory.dto.directory.RootDirectoriesRequest;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class DirectoriesRepository {

  private final MinioClient minioConfig;

  public Iterable<Result<Item>> getFilesInDirectory(DirectoryInfoRequest directoryInfoRequest) {
    return minioConfig.listObjects(
        ListObjectsArgs.builder()
            .bucket(directoryInfoRequest.bucketName())
            .prefix(directoryInfoRequest.pathToDirectory())
            .build());
  }

  public Iterable<Result<Item>> getRootDirectories(RootDirectoriesRequest rootDirectoriesRequest) {
    return minioConfig.listObjects(
        ListObjectsArgs.builder().bucket(rootDirectoriesRequest.bucketName()).prefix("/").build());
  }
}
