package mipt.app.secondmemory.controller;

import io.minio.errors.ErrorResponseException;
import io.minio.errors.InsufficientDataException;
import io.minio.errors.InternalException;
import io.minio.errors.InvalidResponseException;
import io.minio.errors.ServerException;
import io.minio.errors.XmlParserException;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import mipt.app.secondmemory.exception.FileMemoryOverflowException;
import mipt.app.secondmemory.exception.FileServerException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@RequestMapping("/second-memory")
@Tag(name = "File API", description = "Управление файлами")
public interface FilesController {

    ModelAndView download(@PathVariable String bucketName, HttpServletRequest request) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException;

    ResponseEntity<String> uploadSingle(@PathVariable String bucketName, @RequestParam("file") MultipartFile file) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException, FileMemoryOverflowException, FileServerException;

    ResponseEntity<String> rename(@PathVariable String bucketName, @PathVariable String oldKey, @RequestParam(name = "name") String newKey) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException, FileServerException;

    ResponseEntity<String> delete(@PathVariable String bucketName, @PathVariable String key) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException;

    ResponseEntity<String> moveInBucket(@PathVariable String bucketName, @PathVariable String fileName, @RequestParam String oldPath, @RequestParam String newPath) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException;

    ResponseEntity<String> moveBetweenBuckets(String oldBucketName, String newBucketName, String key) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException;

}

