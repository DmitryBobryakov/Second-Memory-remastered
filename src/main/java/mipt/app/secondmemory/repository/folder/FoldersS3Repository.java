package mipt.app.secondmemory.repository.folder;

import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import io.minio.Result;
import io.minio.errors.ErrorResponseException;
import io.minio.errors.InsufficientDataException;
import io.minio.errors.InternalException;
import io.minio.errors.InvalidResponseException;
import io.minio.errors.ServerException;
import io.minio.errors.XmlParserException;
import io.minio.messages.Item;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mipt.app.secondmemory.dto.directory.DirectoryInfoRequest;
import mipt.app.secondmemory.repository.DirectoriesRepository;
import org.springframework.stereotype.Repository;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
@Slf4j
public class FoldersS3Repository {
  private final MinioClient client;
  private final DirectoriesRepository directoriesRepository;

  public void createFolder(String folderName, String pathToFolder, String bucketName)
      throws ServerException,
          InsufficientDataException,
          ErrorResponseException,
          IOException,
          NoSuchAlgorithmException,
          InvalidKeyException,
          InvalidResponseException,
          XmlParserException,
          InternalException {
    log.debug(
        "Функция по созданию папки вызвана в репозитории. Bucket: {}, folderName: {}",
        bucketName,
        folderName);
    client.putObject(
        PutObjectArgs.builder().bucket(bucketName).object(pathToFolder + folderName + "/").stream(
                new ByteArrayInputStream(new byte[] {}), 0, -1)
            .build());
  }

  public void deleteFolder(String bucketName, String folderPrefix)
      throws ServerException,
          InsufficientDataException,
          ErrorResponseException,
          IOException,
          NoSuchAlgorithmException,
          InvalidKeyException,
          InvalidResponseException,
          XmlParserException,
          InternalException {
    List<String> filesNames = getFilesNamesInDirectory(bucketName, folderPrefix);
    for (String fileName : filesNames) {
      client.removeObject(RemoveObjectArgs.builder().bucket(bucketName).object(fileName).build());
    }
  }

  public List<String> getFilesNamesInDirectory(String bucketName, String folderPrefix)
      throws ServerException,
          InsufficientDataException,
          ErrorResponseException,
          IOException,
          NoSuchAlgorithmException,
          InvalidKeyException,
          InvalidResponseException,
          XmlParserException,
          InternalException {
    ArrayList<String> filesNames = new ArrayList<>();
    for (Result<Item> result :
        directoriesRepository.getFilesInDirectory(
            new DirectoryInfoRequest(null, folderPrefix, bucketName))) {
      filesNames.add(result.get().objectName());
    }
    return filesNames;
  }
}
