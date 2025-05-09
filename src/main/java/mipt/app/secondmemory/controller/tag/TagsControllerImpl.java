package mipt.app.secondmemory.controller.tag;

import static mipt.app.secondmemory.entity.RoleType.OWNER;
import static mipt.app.secondmemory.entity.RoleType.WRITER;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mipt.app.secondmemory.dto.tag.FileTagDto;
import mipt.app.secondmemory.dto.tag.TagDto;
import mipt.app.secondmemory.entity.Role;
import mipt.app.secondmemory.entity.Session;
import mipt.app.secondmemory.exception.file.FileNotFoundException;
import mipt.app.secondmemory.exception.role.NoRoleFoundException;
import mipt.app.secondmemory.exception.session.SessionNotFoundException;
import mipt.app.secondmemory.exception.tag.TagNotFoundException;
import mipt.app.secondmemory.repository.RolesRepository;
import mipt.app.secondmemory.repository.SessionsRepository;
import mipt.app.secondmemory.service.TagsService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class TagsControllerImpl implements TagsController {
  private final TagsService tagsService;
  private final SessionsRepository sessionsRepository;
  private final RolesRepository rolesRepository;

  @Override
  public ResponseEntity<TagDto> getTag(Long tagId) throws TagNotFoundException {
    return ResponseEntity.ok(tagsService.getTag(tagId));
  }

  @Override
  public ResponseEntity<FileTagDto> deleteTagByFile(Long tagId, Long fileId, String cookieValue)
      throws TagNotFoundException, FileNotFoundException, SessionNotFoundException {
    Session session =
        sessionsRepository.findByCookie(cookieValue).orElseThrow(SessionNotFoundException::new);
    Role userRole =
        rolesRepository
            .findByUserIdAndFileId(session.getUser().getId(), fileId)
            .orElseThrow(NoRoleFoundException::new);
    if (userRole.getType() != WRITER && userRole.getType() != OWNER) {
      throw new AuthorizationDeniedException("You don't have permission to delete tag");
    }
    return ResponseEntity.ok(tagsService.deleteTagWithFileId(tagId, fileId));
  }

  @Override
  public ResponseEntity<FileTagDto> addTagToFile(Long fileId, String tagName, String cookieValue)
      throws FileNotFoundException, SessionNotFoundException {
    Session session =
        sessionsRepository.findByCookie(cookieValue).orElseThrow(SessionNotFoundException::new);
    Role userRole =
        rolesRepository
            .findByUserIdAndFileId(session.getUser().getId(), fileId)
            .orElseThrow(NoRoleFoundException::new);
    if (userRole.getType() != WRITER && userRole.getType() != OWNER) {
      throw new AuthorizationDeniedException("You don't have permission to add tag");
    }
    return ResponseEntity.status(201).body(tagsService.addTagToFile(fileId, tagName));
  }

  @Override
  public ResponseEntity<List<TagDto>> getAllTagsByFile(Long fileId) {
    return ResponseEntity.ok(tagsService.getAllTagsByFileId(fileId));
  }
}
