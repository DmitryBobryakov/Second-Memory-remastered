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
import mipt.app.secondmemory.config.MinioClientConfig;
import mipt.app.secondmemory.exception.FileMemoryOverflowException;
import mipt.app.secondmemory.exception.FileServerException;
import mipt.app.secondmemory.repository.FilesJpaRepository;
import mipt.app.secondmemory.repository.FilesS3Repository;
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
public class FilesServiceImpl implements FilesService {
    private static final long CAPACITY = 1024 * 1024 * 10;
    private final FilesS3Repository filesS3Repository;
    private final FilesJpaRepository filesJpaRepository;
    private static final MinioClient client = MinioClientConfig.getClient();

    @Override
    public ModelAndView download(String bucketName, String key) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        log.info("Функция по скачиванию файла вызвана в сервисе");
        boolean found = client.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
        if (!found) {
            throw new FileNotFoundException();
        }
        return filesS3Repository.download(bucketName, key);
    }

    @Override
    @Transactional
    public String uploadSingle(String bucketName, MultipartFile file) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException, FileMemoryOverflowException, FileServerException {
        log.info("Функция по загрузке файла вызвана в сервисе");
        boolean found = client.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
        if (!found) {
            client.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
        }
        if (file.getSize() > CAPACITY) {
            throw new FileMemoryOverflowException();
        }
        return filesS3Repository.upload(bucketName, file);
    }

    @Override
    public String rename(String bucketName, String oldKey, String newKey) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException, FileServerException {
        log.info("Функция по переименованию файла вызвана в сервисе");
        if (oldKey.equals(newKey)) {
            return "success";
        }
        boolean found = client.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
        if (!found) {
            throw new FileNotFoundException();
        }
        return filesS3Repository.rename(bucketName, oldKey, newKey);
    }

    @Override
    public String delete(String bucketName, String key) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        log.info("Функция по удалению файла вызвана в сервисе");
        boolean found = client.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
        if (!found) {
            throw new FileNotFoundException();
        }
        return filesS3Repository.delete(bucketName, key);
    }

    @Override
    public String moveInBucket(String bucketName, String fileName, String oldPath, String newPath) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        log.info("Функция по перемещению файла внутри бакета вызвана в репозитории");
        boolean found = client.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
        if (!found) {
            throw new FileNotFoundException();
        }
        return filesS3Repository.moveInBucket(bucketName, fileName, oldPath, newPath);
    }

    @Override
    public String moveBetweenBuckets(String oldBucketName, String newBucketName, String key) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        log.info("Функция по перемещению файла через бакеты вызвана в репозитории");
        boolean foundInOldBucket = client.bucketExists(BucketExistsArgs.builder().bucket(oldBucketName).build());
        boolean foundInNewBucket = client.bucketExists(BucketExistsArgs.builder().bucket(newBucketName).build());
        if (!foundInNewBucket || !foundInOldBucket) {
            throw new FileNotFoundException();
        }
        return filesS3Repository.moveBetweenBuckets(oldBucketName, newBucketName, key);
    }
}
