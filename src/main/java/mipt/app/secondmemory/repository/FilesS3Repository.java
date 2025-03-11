package mipt.app.secondmemory.repository;

import io.minio.errors.ErrorResponseException;
import io.minio.errors.InsufficientDataException;
import io.minio.errors.InternalException;
import io.minio.errors.InvalidResponseException;
import io.minio.errors.ServerException;
import io.minio.errors.XmlParserException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public interface FilesS3Repository {
    ModelAndView download(String bucketName, String key) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException, NoSuchAlgorithmException;

    String upload(String bucketName, MultipartFile file) throws IOException, InsufficientDataException, ErrorResponseException, InvalidKeyException, InvalidResponseException, XmlParserException, NoSuchAlgorithmException, InternalException, ServerException;

    String rename(String bucketName, String oldKey, String newKey) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException;

    String delete(String bucketName, String key) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException;

    String moveInBucket(String bucketName, String fileName, String oldPath, String newPath) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException;

    String moveBetweenBuckets(String oldBucketName, String newBucketName, String key) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException;

}