package mipt.app.secondmemory.mapper;

import mipt.app.secondmemory.dto.tag.TagDto;
import mipt.app.secondmemory.entity.TagEntity;
import org.springframework.stereotype.Component;

@Component
public class TagMapper {
  public TagDto toDto(TagEntity tagEntity) {
    return TagDto.builder().name(tagEntity.getName()).build();
  }
}
