package mipt.app.secondmemory.service;

import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.errors.ErrorResponseException;
import io.minio.errors.InsufficientDataException;
import io.minio.errors.InternalException;
import io.minio.errors.InvalidResponseException;
import io.minio.errors.ServerException;
import io.minio.errors.XmlParserException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mipt.app.secondmemory.configuration.MinioClientConfig;
import mipt.app.secondmemory.exception.FileMemoryOverflowException;
import mipt.app.secondmemory.repository.FilesJpaRepository;
import mipt.app.secondmemory.repository.FilesS3RepositoryImpl;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Service
@Slf4j
@RequiredArgsConstructor
public class FilesServiceImpl {
    private static final long MAX_CAPACITY = 1024 * 1024 * 10;
    private final FilesS3RepositoryImpl filesS3Repository;
    private final FilesJpaRepository filesJpaRepository;
    private static final MinioClient client = MinioClientConfig.getClient();


    public ModelAndView download(String bucketName, String key) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        log.info("Функция по скачиванию файла вызвана в сервисе");
        boolean found = client.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
        if (!found) {
            throw new FileNotFoundException();
        }
        return filesS3Repository.download(bucketName, key);
    }


    @Transactional
    public void uploadSingle(String bucketName, MultipartFile file) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException, FileMemoryOverflowException {
        log.info("Функция по загрузке файла вызвана в сервисе");
        boolean found = client.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
        if (!found) {
            client.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
        }
        if (file.getSize() > MAX_CAPACITY) {
            throw new FileMemoryOverflowException();
        }
        filesS3Repository.upload(bucketName, file);
    }


    public void rename(String bucketName, String oldKey, String newKey) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        log.info("Функция по переименованию файла вызвана в сервисе");
        if (oldKey.equals(newKey)) {
            return;
        }
        boolean found = client.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
        if (!found) {
            throw new FileNotFoundException();
        }
        filesS3Repository.rename(bucketName, oldKey, newKey);
    }


    public void delete(String bucketName, String key) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        log.info("Функция по удалению файла вызвана в сервисе");
        boolean found = client.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
        if (!found) {
            throw new FileNotFoundException();
        }
        filesS3Repository.delete(bucketName, key);
    }


    public void moveInBucket(String bucketName, String fileName, String oldPath, String newPath) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        log.info("Функция по перемещению файла внутри бакета вызвана в репозитории");
        boolean found = client.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
        if (!found) {
            throw new FileNotFoundException();
        }
        filesS3Repository.moveInBucket(bucketName, fileName, oldPath, newPath);
    }


    public void moveBetweenBuckets(String oldBucketName, String newBucketName, String key) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        log.info("Функция по перемещению файла через бакеты вызвана в репозитории");
        boolean foundInOldBucket = client.bucketExists(BucketExistsArgs.builder().bucket(oldBucketName).build());
        boolean foundInNewBucket = client.bucketExists(BucketExistsArgs.builder().bucket(newBucketName).build());
        if (!foundInNewBucket || !foundInOldBucket) {
            throw new FileNotFoundException();
        }
        filesS3Repository.moveBetweenBuckets(oldBucketName, newBucketName, key);
    }
}
