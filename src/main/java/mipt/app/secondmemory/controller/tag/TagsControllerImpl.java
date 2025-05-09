package mipt.app.secondmemory.controller.tag;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mipt.app.secondmemory.dto.tag.FileTagDto;
import mipt.app.secondmemory.dto.tag.TagDto;
import mipt.app.secondmemory.exception.file.FileNotFoundException;
import mipt.app.secondmemory.exception.tag.TagNotFoundException;
import mipt.app.secondmemory.service.TagsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class TagsControllerImpl implements TagsController {
  private final TagsService tagsService;

  @Override
  public ResponseEntity<TagDto> getTag(Long tagId) throws TagNotFoundException {
    return ResponseEntity.ok(tagsService.getTag(tagId));
  }

  @Override
  public ResponseEntity<FileTagDto> deleteTagByFile(Long tagId, Long fileId)
      throws TagNotFoundException, FileNotFoundException {
    return ResponseEntity.ok(tagsService.deleteTagWithFileId(tagId, fileId));
  }

  @Override
  public ResponseEntity<FileTagDto> addTagToFile(Long fileId, String tagName)
      throws FileNotFoundException {
    return ResponseEntity.status(201).body(tagsService.addTagToFile(fileId, tagName));
  }

  @Override
  public ResponseEntity<List<TagDto>> getAllTagsByFile(Long fileId) {
    return ResponseEntity.ok(tagsService.getAllTagsByFileId(fileId));
  }
}
