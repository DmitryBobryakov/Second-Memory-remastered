package mipt.app.secondmemory.controller.tag;

import io.swagger.v3.oas.annotations.tags.Tag;
import mipt.app.secondmemory.dto.tag.TagDto;
import mipt.app.secondmemory.entity.TagEntity;
import mipt.app.secondmemory.exception.tag.TagNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "Tags API", description = "Управление тегами")
public interface TagsController {

  @GetMapping("/tags/get/{tagId}")
  ResponseEntity<TagDto> get(@PathVariable(name = "tagId") Long tagId) throws TagNotFoundException;

  @DeleteMapping("/tags/delete/{tagId}")
  ResponseEntity<TagDto> delete(@PathVariable(name = "tagId") Long tagId)
      throws TagNotFoundException;

  @PutMapping("/tags/put/{tagId}")
  ResponseEntity<TagDto> rename(
      @PathVariable(name = "tagId") Long tagId, @RequestBody TagEntity newTagEntity)
      throws TagNotFoundException;

  @PostMapping("/tags/create")
  ResponseEntity<TagDto> create(@RequestBody TagEntity tagEntity);
}
