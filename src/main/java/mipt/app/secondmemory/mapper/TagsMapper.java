package mipt.app.secondmemory.mapper;

import lombok.experimental.UtilityClass;
import mipt.app.secondmemory.dto.tag.TagDto;
import mipt.app.secondmemory.entity.TagEntity;

import java.sql.Timestamp;
import java.time.Instant;

@UtilityClass
public class TagsMapper {
  public TagDto toDto(TagEntity tagEntity) {
    return TagDto.builder()
        .id(tagEntity.getId())
        .lastModifiedTime(Timestamp.from(Instant.now()).getTime())
        .name(tagEntity.getName())
        .build();
  }
}
