package mipt.app.secondmemory.controller.file;

import io.minio.errors.ErrorResponseException;
import io.minio.errors.InsufficientDataException;
import io.minio.errors.InternalException;
import io.minio.errors.InvalidResponseException;
import io.minio.errors.ServerException;
import io.minio.errors.XmlParserException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mipt.app.secondmemory.exception.file.FileNotFoundException;
import mipt.app.secondmemory.service.FilesService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class FilesDownloadControllerImpl implements FilesDownloadController {
  private final FilesService filesService;

  public ModelAndView download(String bucketName, String key)
      throws ServerException,
          InsufficientDataException,
          FileNotFoundException,
          ErrorResponseException,
          IOException,
          NoSuchAlgorithmException,
          InvalidKeyException,
          InvalidResponseException,
          XmlParserException,
          InternalException {
    return filesService.downloadFile(bucketName, key);
  }
}
