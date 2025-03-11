package mipt.app.secondmemory.service;


import mipt.app.secondmemory.dto.TagDto;
import mipt.app.secondmemory.entity.Tag;
import mipt.app.secondmemory.exception.TagNotFoundException;

public interface TagsService {

    TagDto getTag(Long tagId) throws TagNotFoundException;

    TagDto putTag(Long tagId, Tag newTag) throws TagNotFoundException;

    TagDto deleteTag(Long tagId) throws TagNotFoundException;

    TagDto createTag(Tag tag);
}
