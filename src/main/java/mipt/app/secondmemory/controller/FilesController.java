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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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

    @GetMapping("/files/download/{bucketName}")
    ModelAndView download(@PathVariable(name = "bucketName") String bucketName, HttpServletRequest request) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException;

    @PostMapping("/files/upload/{bucketName}")
    ResponseEntity<Void> uploadSingle(@PathVariable(name = "bucketName") String bucketName, @RequestParam("file") MultipartFile file) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException, FileMemoryOverflowException, FileServerException;

    @PatchMapping("/files/rename/{bucketName}/{oldKey}")
    ResponseEntity<Void> rename(@PathVariable(name = "bucketName") String bucketName, @PathVariable(name = "oldKey") String oldKey, @RequestParam(name = "name") String newKey) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException, FileServerException;

    @DeleteMapping("/files/delete/{bucketName}/{key}")
    ResponseEntity<Void> delete(@PathVariable(name = "bucketName") String bucketName, @PathVariable(name = "key") String key) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException;

    @PostMapping("/files/moveInBucket/{bucketName}/{fileName}")
    ResponseEntity<Void> moveInBucket(@PathVariable(name = "bucketName") String bucketName, @PathVariable(name = "fileName") String fileName, @RequestParam String oldPath, @RequestParam String newPath) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException;

    @PostMapping("/files/moveInBucket/{oldBucketName}/{newBucketName}")
    ResponseEntity<Void> moveBetweenBuckets(@PathVariable(name = "oldBucketName") String oldBucketName, @PathVariable(name = "newBucketName") String newBucketName, @RequestParam String key) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException;

}
