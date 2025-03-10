package mipt.app.secondmemory.controller;


import io.minio.errors.ErrorResponseException;
import io.minio.errors.InsufficientDataException;
import io.minio.errors.InternalException;
import io.minio.errors.InvalidResponseException;
import io.minio.errors.ServerException;
import io.minio.errors.XmlParserException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mipt.app.secondmemory.exception.FileMemoryOverflowException;
import mipt.app.secondmemory.service.FilesService;
import org.springframework.http.ResponseEntity;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

@RestController
@Slf4j
@RequiredArgsConstructor
public class FilesControllerImpl implements FilesController {
    private final FilesService filesService;

    @Override
    @GetMapping("/files/download/{bucketName}")
    public ModelAndView download(@PathVariable String bucketName, HttpServletRequest request) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        String key = new AntPathMatcher()
                .extractPathWithinPattern(
                        request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE).toString(),
                        request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE).toString());
        return filesService.download(bucketName, key);
    }

    @Override
    @PostMapping("/files/upload/{bucketName}")
    public ResponseEntity<Map<String, String>> uploadSingle(@PathVariable String bucketName, @RequestParam("file") MultipartFile file) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException, FileMemoryOverflowException {
        return ResponseEntity.ok(filesService.uploadSingle(bucketName, file));
    }

    @Override
    @PatchMapping("/files/rename/{bucketName}/{oldKey}")
    public ResponseEntity<String> rename(@PathVariable String bucketName, @PathVariable String oldKey, @RequestParam(name = "name") String newKey) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        return ResponseEntity.ok(filesService.rename(bucketName, oldKey, newKey));
    }

    @Override
    @DeleteMapping("/files/delete/{bucketName}/{key}")
    public ResponseEntity<String> delete(@PathVariable String bucketName, @PathVariable String key) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        return ResponseEntity.ok(filesService.delete(bucketName, key));
    }

    @Override
    @PostMapping("/files/moveInBucket/{bucketName}/{fileName}")
    public ResponseEntity<String> moveInBucket(@PathVariable String bucketName, @PathVariable String fileName, @RequestParam String oldPath, @RequestParam String newPath) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        return ResponseEntity.ok(filesService.moveInBucket(bucketName, fileName, oldPath, newPath));
    }

    @Override
    @PostMapping("/files/moveInBucket/{oldBucketName}/{newBucketName}")
    public ResponseEntity<String> moveBetweenBuckets(@PathVariable String oldBucketName, @PathVariable String newBucketName, @RequestParam String key) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        return ResponseEntity.ok(filesService.moveBetweenBuckets(oldBucketName, newBucketName, key));
    }
}
