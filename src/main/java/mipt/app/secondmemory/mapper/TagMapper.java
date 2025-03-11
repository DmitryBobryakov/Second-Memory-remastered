package mipt.app.secondmemory.mapper;


import mipt.app.secondmemory.dto.TagDto;
import mipt.app.secondmemory.entity.Tag;
import org.springframework.stereotype.Component;

@Component
public class TagMapper {
    public TagDto toDto(Tag tag) {
        String name = tag.getName();
        Long id = tag.getId();
        return new TagDto(id, name);
    }

    public Tag toTag(TagDto tagDto) {
        return Tag.builder().name(tagDto.name()).build();
    }
}