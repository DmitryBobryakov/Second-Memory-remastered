package mipt.app.secondmemory.controller.file;

import static mipt.app.secondmemory.entity.RoleType.OWNER;
import static mipt.app.secondmemory.entity.RoleType.READER;
import static mipt.app.secondmemory.entity.RoleType.WRITER;

import io.minio.errors.ErrorResponseException;
import io.minio.errors.InsufficientDataException;
import io.minio.errors.InternalException;
import io.minio.errors.InvalidResponseException;
import io.minio.errors.ServerException;
import io.minio.errors.XmlParserException;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mipt.app.secondmemory.entity.Role;
import mipt.app.secondmemory.entity.Session;
import mipt.app.secondmemory.exception.file.FileNotFoundException;
import mipt.app.secondmemory.exception.role.NoRoleFoundException;
import mipt.app.secondmemory.exception.session.SessionNotFoundException;
import mipt.app.secondmemory.repository.RolesRepository;
import mipt.app.secondmemory.repository.SessionsRepository;
import mipt.app.secondmemory.service.FilesService;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.stereotype.Controller;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@Slf4j
@RequiredArgsConstructor
public class RedirectFileDownloadControllerImpl implements RedirectFileDownloadController {
  private final FilesService filesService;
  private final SessionsRepository sessionsRepository;
  private final RolesRepository rolesRepository;

  public ModelAndView download(
      String bucketName, Long fileId, HttpServletRequest request, String cookieValue)
      throws SessionNotFoundException,
          ServerException,
          InsufficientDataException,
          FileNotFoundException,
          ErrorResponseException,
          IOException,
          NoSuchAlgorithmException,
          InvalidKeyException,
          InvalidResponseException,
          XmlParserException,
          InternalException {
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
    String key =
        new AntPathMatcher()
            .extractPathWithinPattern(
                request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE).toString(),
                request
                    .getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE)
                    .toString());
    return filesService.download(bucketName, key);
  }
}
