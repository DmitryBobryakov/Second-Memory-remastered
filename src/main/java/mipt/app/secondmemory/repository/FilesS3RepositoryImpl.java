package mipt.app.secondmemory.repository;

import io.minio.CopyObjectArgs;
import io.minio.CopySource;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import io.minio.errors.ErrorResponseException;
import io.minio.errors.InsufficientDataException;
import io.minio.errors.InternalException;
import io.minio.errors.InvalidResponseException;
import io.minio.errors.ServerException;
import io.minio.errors.XmlParserException;
import io.minio.http.Method;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

@Repository
@Slf4j
@RequiredArgsConstructor
public class FilesS3RepositoryImpl {
  private final MinioClient client;

  public ModelAndView download(String bucketName, String key)
      throws ServerException,
          InsufficientDataException,
          ErrorResponseException,
          IOException,
          InvalidKeyException,
          InvalidResponseException,
          XmlParserException,
          InternalException,
          NoSuchAlgorithmException {
    log.debug(
        "Функция по скачиванию файла вызвана в репозитории. Bucket: {}, key: {}", bucketName, key);
    String url =
        client.getPresignedObjectUrl(
            GetPresignedObjectUrlArgs.builder()
                .method(Method.GET)
                .bucket(bucketName)
                .object(key)
                .build());
    return new ModelAndView("redirect:" + url);
  }

  public void upload(String bucketName, MultipartFile file)
      throws IOException,
          InsufficientDataException,
          ErrorResponseException,
          InvalidKeyException,
          InvalidResponseException,
          XmlParserException,
          NoSuchAlgorithmException,
          InternalException,
          ServerException {
    log.debug(
        "Функция по загрузке файла вызвана в репозитории. Bucket: {}, key: {}",
        bucketName,
        file.getOriginalFilename());

    String fileName = file.getOriginalFilename();
    InputStream fileInputStream = file.getInputStream();

    client.putObject(
        PutObjectArgs.builder().bucket(bucketName).object(fileName).stream(
                fileInputStream, -1, 10485760)
            .build());
  }

  public void rename(String bucketName, String oldKey, String newKey)
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
        "Функция по переименованию файла вызвана в репозитории. Bucket: {}, oldKey: {}, newKey {}",
        bucketName,
        oldKey,
        newKey);
    client.copyObject(
        CopyObjectArgs.builder()
            .bucket(bucketName)
            .object(newKey)
            .source(CopySource.builder().bucket(bucketName).object(oldKey).build())
            .build());
    delete(bucketName, oldKey);
  }

  public void delete(String bucketName, String key)
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
        "Функция по удалению файла вызвана в репозитории. Bucket: {}, key: {}", bucketName, key);
    client.removeObject(RemoveObjectArgs.builder().bucket(bucketName).object(key).build());
  }

  public void move(
      String oldBucketName, String newBucketName, String fileName, String oldPath, String newPath)
      throws ServerException,
          InsufficientDataException,
          ErrorResponseException,
          IOException,
          NoSuchAlgorithmException,
          InvalidKeyException,
          InvalidResponseException,
          XmlParserException,
          InternalException {
    String oldKey = oldPath + "/" + fileName;
    String newKey = newPath + "/" + fileName;
    log.debug(
        "Функция по перемещению файла вызвана в репозитории. OldBucket: {}, newBucket: {}, oldKey: {}, newKey: {}",
        oldBucketName,
        newBucketName,
        oldKey,
        newKey);
    client.copyObject(
        CopyObjectArgs.builder()
            .bucket(newBucketName)
            .object(newKey)
            .source(CopySource.builder().bucket(oldBucketName).object(oldKey).build())
            .build());
    delete(oldBucketName, oldKey);
  }
}
