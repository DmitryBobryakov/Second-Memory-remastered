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

import jakarta.servlet.http.Part;
import lombok.RequiredArgsConstructor;
import java.sql.Timestamp;
import java.time.Instant;
import lombok.extern.slf4j.Slf4j;
import mipt.app.secondmemory.dto.file.FileInfoResponse;
import mipt.app.secondmemory.entity.FileEntity;
import mipt.app.secondmemory.mapper.FilesMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.ModelAndView;

@Repository
@Slf4j
@RequiredArgsConstructor
public class FilesS3RepositoryImpl {
  private final MinioClient client;
  private final FilesRepository filesRepository;
  private final BucketsRepository bucketsRepository;

  public ModelAndView downloadFile(String bucketName, String key)
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

  @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT)
  public void uploadFile(String bucketName, Part file)
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
        file.getName());

    String fileName = file.getSubmittedFileName();
    InputStream fileInputStream = file.getInputStream();

    client.putObject(
        PutObjectArgs.builder().bucket(bucketName).object(fileName).stream(
                fileInputStream, -1, 10485760)
            .build());
  }

  public void renameFile(String bucketName, String oldKey, String newKey)
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
    deleteFile(bucketName, oldKey);
  }

  public void deleteFile(String bucketName, String key)
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

  public void moveFile(
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
    deleteFile(oldBucketName, oldKey);
  }

  public void uploadFileToFolder(String bucketName, Part file, String pathToFolder)
      throws IOException,
          ServerException,
          InsufficientDataException,
          ErrorResponseException,
          NoSuchAlgorithmException,
          InvalidKeyException,
          InvalidResponseException,
          XmlParserException,
          InternalException {
    String fileName = file.getSubmittedFileName();
    InputStream fileInputStream = file.getInputStream();

    client.putObject(
        PutObjectArgs.builder().bucket(bucketName).object(pathToFolder + "/" + fileName).stream(
                fileInputStream, -1, 10485760)
            .build());
  }
}
