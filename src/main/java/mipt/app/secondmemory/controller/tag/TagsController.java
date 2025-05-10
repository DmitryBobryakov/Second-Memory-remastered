package mipt.app.secondmemory.controller.tag;

import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import mipt.app.secondmemory.dto.tag.TagDto;
import mipt.app.secondmemory.exception.file.FileNotFoundException;
import mipt.app.secondmemory.exception.session.SessionNotFoundException;
import mipt.app.secondmemory.exception.tag.TagNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "Tags API", description = "Управление тегами")
public interface TagsController {

  @GetMapping("/tags/get/{tagId}")
  ResponseEntity<TagDto> getTag(@PathVariable(name = "tagId") Long tagId)
      throws TagNotFoundException;

  @DeleteMapping("/tags/delete/by/file")
  ResponseEntity<TagDto> deleteTagByFile(
      @RequestParam(name = "tagId") Long tagId,
      @RequestParam(name = "fileId") Long fileId,
      @CookieValue("token") String cookieValue)
      throws TagNotFoundException, FileNotFoundException, SessionNotFoundException;

  @PostMapping("/tags/add/tag/to/file")
  ResponseEntity<TagDto> addTagToFile(
      @RequestParam(name = "fileId") Long fileId,
      @RequestParam(name = "tagName") String tagName,
      @CookieValue("token") String cookieValue)
      throws FileNotFoundException, SessionNotFoundException;

  @PostMapping("/tags/get/all/by/file")
  ResponseEntity<List<TagDto>> getAllTagsByFile(@RequestParam(name = "fileId") Long fileId);
}
