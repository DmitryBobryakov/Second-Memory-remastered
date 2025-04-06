package mipt.app.secondmemory.controller;

import mipt.app.secondmemory.dto.TagDto;
import mipt.app.secondmemory.entity.Tag;
import mipt.app.secondmemory.exception.TagNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/second-memory")
public interface TagsController {

  @GetMapping("/tags/get/{tagId}")
  ResponseEntity<TagDto> get(@PathVariable(name = "tagId") Long tagId) throws TagNotFoundException;

  @DeleteMapping("/tags/delete/{tagId}")
  ResponseEntity<TagDto> delete(@PathVariable(name = "tagId") Long tagId)
      throws TagNotFoundException;

  @PutMapping("/tags/put/{tagId}")
  ResponseEntity<TagDto> rename(@PathVariable(name = "tagId") Long tagId, @RequestBody Tag newTag)
      throws TagNotFoundException;

  @PostMapping("/tags/create")
  ResponseEntity<TagDto> create(@RequestBody Tag tag);
}
