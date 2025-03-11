package mipt.app.secondmemory.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mipt.app.secondmemory.dto.TagDto;
import mipt.app.secondmemory.entity.Tag;
import mipt.app.secondmemory.exception.TagNotFoundException;
import mipt.app.secondmemory.service.TagsServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
public class TagsControllerImpl implements TagsController {
    private final TagsServiceImpl tagsService;

    @GetMapping("/tags/get/{tagId}")
    @Override
    public ResponseEntity<TagDto> getTag(@PathVariable Long tagId) throws TagNotFoundException {
        TagDto tagDto = tagsService.getTag(tagId);
        return ResponseEntity.ok(tagDto);
    }

    @DeleteMapping("/tags/delete/{tagId}")
    @Override
    public ResponseEntity<TagDto> deleteTag(@PathVariable Long tagId) throws TagNotFoundException {
        TagDto tagDto = tagsService.deleteTag(tagId);
        return ResponseEntity.ok(tagDto);
    }


    @PutMapping("/tags/put/{tagId}")
    @Override
    public ResponseEntity<TagDto> putTag(@PathVariable Long tagId, @RequestBody Tag newTag) throws TagNotFoundException {
        TagDto tagDto = tagsService.putTag(tagId, newTag);
        return ResponseEntity.ok(tagDto);
    }

    @PostMapping("/tags/create")
    @Override
    public ResponseEntity<TagDto> createTag(@RequestBody Tag tag) {
        TagDto tagDto = tagsService.createTag(tag);
        return ResponseEntity.status(201).body(tagDto);
    }
}
