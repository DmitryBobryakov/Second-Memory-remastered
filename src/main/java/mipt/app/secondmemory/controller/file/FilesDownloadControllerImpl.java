package mipt.app.secondmemory.controller.file;

import static mipt.app.secondmemory.entity.RoleType.OWNER;
import static mipt.app.secondmemory.entity.RoleType.READER;
import static mipt.app.secondmemory.entity.RoleType.WRITER;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.minio.errors.ErrorResponseException;
import io.minio.errors.InsufficientDataException;
import io.minio.errors.InternalException;
import io.minio.errors.InvalidResponseException;
import io.minio.errors.ServerException;
import io.minio.errors.XmlParserException;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mipt.app.secondmemory.entity.Role;
import mipt.app.secondmemory.entity.Session;
import mipt.app.secondmemory.exception.directory.BucketNotFoundException;
import mipt.app.secondmemory.exception.file.FileNotFoundException;
import mipt.app.secondmemory.exception.role.NoRoleFoundException;
import mipt.app.secondmemory.exception.session.SessionNotFoundException;
import mipt.app.secondmemory.repository.RolesRepository;
import mipt.app.secondmemory.repository.SessionsRepository;
import mipt.app.secondmemory.service.FilesService;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class FilesDownloadControllerImpl implements FilesDownloadController {
  private final FilesService filesService;
  private final MeterRegistry registry;
  private final SessionsRepository sessionsRepository;
  private final RolesRepository rolesRepository;

  private Counter getFilesRequestCounter(String type) {
    return Counter.builder("files.requests")
        .description("Number of files requests by type")
        .tags("type", type)
        .register(registry);
  }

  public ModelAndView download(Long fileId, String cookieValue)
      throws ServerException,
          InsufficientDataException,
          FileNotFoundException,
          ErrorResponseException,
          IOException,
          NoSuchAlgorithmException,
          InvalidKeyException,
          InvalidResponseException,
          XmlParserException,
          InternalException,
          BucketNotFoundException,
          SessionNotFoundException {
    getFilesRequestCounter("download").increment();
    Session session =
        sessionsRepository.findByCookie(cookieValue).orElseThrow(SessionNotFoundException::new);
    Role userRole =
        rolesRepository
            .findByUserIdAndFileId(session.getUser().getId(), fileId)
            .orElseThrow(NoRoleFoundException::new);
    if (userRole.getType() != READER
        && userRole.getType() != WRITER
        && userRole.getType() != OWNER) {
      throw new AuthorizationDeniedException("You don't have permission to download file");
    }
    return filesService.downloadFile(fileId);
  }
}
