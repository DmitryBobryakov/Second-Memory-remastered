package mipt.app.secondmemory.mapper;

import mipt.app.secondmemory.dto.tag.TagDto;
import mipt.app.secondmemory.entity.TagEntity;
import org.springframework.stereotype.Component;

@Component
public class TagMapper {
  public TagDto toDto(TagEntity tagEntity) {
    String name = tagEntity.getName();
    Long id = tagEntity.getId();
    return new TagDto(id, name);
  }

  public TagEntity toTag(TagDto tagDto) {
    return TagEntity.builder().name(tagDto.name()).build();
  }
}
