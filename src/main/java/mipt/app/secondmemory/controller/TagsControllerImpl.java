package mipt.app.secondmemory.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mipt.app.secondmemory.dto.TagDto;
import mipt.app.secondmemory.entity.Tag;
import mipt.app.secondmemory.exception.TagNotFoundException;
import mipt.app.secondmemory.service.TagsServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
public class TagsControllerImpl implements TagsController {
    private final TagsServiceImpl tagsService;

    @Override
    public ResponseEntity<TagDto> getTag(Long tagId) throws TagNotFoundException {
        TagDto tagDto = tagsService.getTag(tagId);
        return ResponseEntity.ok(tagDto);
    }

    @Override
    public ResponseEntity<TagDto> deleteTag(Long tagId) throws TagNotFoundException {
        TagDto tagDto = tagsService.deleteTag(tagId);
        return ResponseEntity.ok(tagDto);
    }


    @Override
    public ResponseEntity<TagDto> putTag(Long tagId, Tag newTag) throws TagNotFoundException {
        TagDto tagDto = tagsService.putTag(tagId, newTag);
        return ResponseEntity.ok(tagDto);
    }

    @Override
    public ResponseEntity<TagDto> createTag(Tag tag) {
        TagDto tagDto = tagsService.createTag(tag);
        return ResponseEntity.status(201).body(tagDto);
    }
}
