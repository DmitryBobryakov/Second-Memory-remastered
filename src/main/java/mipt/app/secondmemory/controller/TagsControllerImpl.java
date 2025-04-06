package mipt.app.secondmemory.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mipt.app.secondmemory.dto.TagDto;
import mipt.app.secondmemory.entity.Tag;
import mipt.app.secondmemory.exception.TagNotFoundException;
import mipt.app.secondmemory.service.TagsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
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
  public ResponseEntity<TagDto> rename(Long tagId, Tag newTag) throws TagNotFoundException {
    TagDto tagDto = tagsService.replace(tagId, newTag);
    return ResponseEntity.ok(tagDto);
  }

  @Override
  public ResponseEntity<TagDto> create(Tag tag) {
    TagDto tagDto = tagsService.create(tag);
    return ResponseEntity.status(201).body(tagDto);
  }
}
