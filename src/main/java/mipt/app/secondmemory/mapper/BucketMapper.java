package mipt.app.secondmemory.mapper;

import mipt.app.secondmemory.dto.directory.BucketDto;
import mipt.app.secondmemory.entity.BucketEntity;
import org.springframework.stereotype.Component;

@Component
public class BucketMapper {
  public BucketDto toDto(BucketEntity bucketEntity) {
    return BucketDto.builder().name(bucketEntity.getName()).build();
  }
}
