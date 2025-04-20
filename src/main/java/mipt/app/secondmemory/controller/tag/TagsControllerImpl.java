package mipt.app.secondmemory.controller.tag;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mipt.app.secondmemory.dto.tag.TagDto;
import mipt.app.secondmemory.entity.TagEntity;
import mipt.app.secondmemory.exception.tag.TagNotFoundException;
import mipt.app.secondmemory.service.TagsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class TagsControllerImpl implements TagsController {
  private final TagsService tagsService;

  @Override
  public ResponseEntity<TagDto> get(Long tagId) throws TagNotFoundException {
    TagDto tagDto = tagsService.get(tagId);
    return ResponseEntity.ok(tagDto);
  }

  @Override
  public ResponseEntity<TagDto> delete(Long tagId) throws TagNotFoundException {
    TagDto tagDto = tagsService.delete(tagId);
    return ResponseEntity.ok(tagDto);
  }

  @Override
  public ResponseEntity<TagDto> rename(Long tagId, TagEntity newTagEntity)
      throws TagNotFoundException {
    TagDto tagDto = tagsService.replace(tagId, newTagEntity);
    return ResponseEntity.ok(tagDto);
  }

  @Override
  public ResponseEntity<TagDto> create(TagEntity tagEntity) {
    TagDto tagDto = tagsService.create(tagEntity);
    return ResponseEntity.status(201).body(tagDto);
  }
}
