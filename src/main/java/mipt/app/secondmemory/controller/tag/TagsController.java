package mipt.app.secondmemory.controller.tag;

import io.swagger.v3.oas.annotations.tags.Tag;
import mipt.app.secondmemory.dto.tag.FileTagDto;
import mipt.app.secondmemory.dto.tag.TagDto;
import mipt.app.secondmemory.exception.file.FileNotFoundException;
import mipt.app.secondmemory.exception.tag.TagNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Tag(name = "Tags API", description = "Управление тегами")
public interface TagsController {

  @GetMapping("/tags/get/{tagId}")
  ResponseEntity<TagDto> getTag(@PathVariable(name = "tagId") Long tagId)
      throws TagNotFoundException;

  @DeleteMapping("/tags/delete/by/file/")
  ResponseEntity<FileTagDto> deleteTagByFile(
      @RequestParam(name = "tagId") Long tagId, @RequestParam(name = "fileId") Long fileId)
      throws TagNotFoundException, FileNotFoundException;

  @PostMapping("/tags/add/tag/to/file")
  ResponseEntity<FileTagDto> addTagToFile(
      @RequestParam(name = "fileId") Long fileId, @RequestParam(name = "tagName") String tagName)
      throws FileNotFoundException;

  @PostMapping("/tags/get/all/by/file")
  ResponseEntity<List<TagDto>> getAllTagsByFile(@RequestParam(name = "fileId") Long fileId);
}
