package mipt.app.secondmemory.controller;

import mipt.app.secondmemory.dto.TagDto;
import mipt.app.secondmemory.entity.Tag;
import mipt.app.secondmemory.exception.TagNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

public interface TagsController {

    ResponseEntity<TagDto> getTag(@PathVariable Long tagId) throws TagNotFoundException;

    ResponseEntity<TagDto> deleteTag(@PathVariable Long tagId) throws TagNotFoundException;

    ResponseEntity<TagDto> putTag(@PathVariable Long tagId, Tag newTag) throws TagNotFoundException;

    ResponseEntity<TagDto> createTag(Tag tag);
}
