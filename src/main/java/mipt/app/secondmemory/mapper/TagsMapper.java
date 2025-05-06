package mipt.app.secondmemory.mapper;

import lombok.experimental.UtilityClass;
import mipt.app.secondmemory.dto.tag.TagDto;
import mipt.app.secondmemory.entity.TagEntity;

@UtilityClass
public class TagsMapper {
  public TagDto toDto(TagEntity tagEntity) {
    return TagDto.builder().id(tagEntity.getId()).name(tagEntity.getName()).build();
  }
}
