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
import lombok.extern.slf4j.Slf4j;
import mipt.app.secondmemory.config.MinioClientConfig;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Repository
@Slf4j
public class FilesS3RepositoryImpl implements FilesS3Repository {
    private static final MinioClient client = MinioClientConfig.getClient();

    @Override
    public ModelAndView download(String bucketName, String key) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException, NoSuchAlgorithmException {
        log.info("Функция по скачиванию файла вызвана в репозитории");
        String url =
                client.getPresignedObjectUrl(
                        GetPresignedObjectUrlArgs.builder()
                                .method(Method.GET)
                                .bucket(bucketName)
                                .object(key)
                                .build());
        return new ModelAndView("redirect:" + url);
    }

    @Override
    public String upload(String bucketName, MultipartFile file) throws IOException, InsufficientDataException, ErrorResponseException, InvalidKeyException, InvalidResponseException, XmlParserException, NoSuchAlgorithmException, InternalException, ServerException {
        log.info("Функция по загрузке файла вызвана в репозитории");

        String fileName = file.getOriginalFilename();
        InputStream fileInputStream = file.getInputStream();

        client.putObject(
                PutObjectArgs.builder()
                        .bucket(bucketName)
                        .object(fileName)
                        .stream(fileInputStream, -1, 10485760)
                        .build());
        return "success";
    }

    @Override
    public String rename(String bucketName, String oldKey, String newKey) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        log.info("Функция по переименованию файла вызвана в репозитории");
        client.copyObject(
                CopyObjectArgs.builder()
                        .bucket(bucketName)
                        .object(newKey)
                        .source(
                                CopySource.builder()
                                        .bucket(bucketName)
                                        .object(oldKey)
                                        .build())
                        .build());
        delete(bucketName, oldKey);
        return "success";
    }

    @Override
    public String delete(String bucketName, String key) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        log.info("Функция по удалению файла вызвана в репозитории");
        client.removeObject(
                RemoveObjectArgs.builder().bucket(bucketName).object(key).build());
        return "success";
    }


    @Override
    public String moveInBucket(String bucketName, String fileName, String oldPath, String newPath) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        log.info("Функция по перемещению файла внутри бакета вызвана в репозитории");
        String key = oldPath + "/" + fileName;
        client.copyObject(
                CopyObjectArgs.builder()
                        .bucket(bucketName)
                        .object(newPath + "/" + fileName)
                        .source(
                                CopySource.builder()
                                        .bucket(bucketName)
                                        .object(key)
                                        .build())
                        .build());
        delete(bucketName, key);
        return "success";
    }

    @Override
    public String moveBetweenBuckets(String oldBucketName, String newBucketName, String key) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        log.info("Функция по перемещению файла через баакеты вызвана в репозитории");
        client.copyObject(
                CopyObjectArgs.builder()
                        .bucket(newBucketName)
                        .object(key)
                        .source(
                                CopySource.builder()
                                        .bucket(oldBucketName)
                                        .object(key)
                                        .build())
                        .build());
        delete(oldBucketName, key);
        return "success";
    }
}
