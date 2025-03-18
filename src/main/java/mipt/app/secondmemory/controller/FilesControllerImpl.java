package mipt.app.secondmemory.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import mipt.app.secondmemory.service.FilesServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.ModelAndView;


@Controller
@Slf4j
@RequiredArgsConstructor
public class FilesControllerImpl implements FilesController {
    private final FilesServiceImpl filesService;

    @Override
    @SneakyThrows
    public ModelAndView download(String bucketName, HttpServletRequest request) {
        String key = new AntPathMatcher()
                .extractPathWithinPattern(
                        request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE).toString(),
                        request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE).toString());
        return filesService.download(bucketName, key);
    }

    @Override
    @SneakyThrows
    public ResponseEntity<Void> uploadSingle(String bucketName, MultipartFile file) {
        filesService.uploadSingle(bucketName, file);
        return ResponseEntity.ok().build();
    }

    @Override
    @SneakyThrows
    public ResponseEntity<Void> rename(String bucketName, String oldKey, String newKey) {
        filesService.rename(bucketName, oldKey, newKey);
        return ResponseEntity.ok().build();
    }

    @Override
    @SneakyThrows
    public ResponseEntity<Void> delete(String bucketName, String key) {
        filesService.delete(bucketName, key);
        return ResponseEntity.ok().build();
    }

    @Override
    @SneakyThrows
    public ResponseEntity<Void> moveInBucket(String bucketName, String fileName, String oldPath, String newPath) {
        filesService.moveInBucket(bucketName, fileName, oldPath, newPath);
        return ResponseEntity.ok().build();
    }

    @Override
    @SneakyThrows
    public ResponseEntity<Void> moveBetweenBuckets(String oldBucketName, String newBucketName, String key) {
        filesService.moveBetweenBuckets(oldBucketName, newBucketName, key);
        return ResponseEntity.ok().build();
    }
}
