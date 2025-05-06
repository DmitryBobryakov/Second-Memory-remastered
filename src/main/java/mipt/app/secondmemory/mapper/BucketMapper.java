package mipt.app.secondmemory.mapper;

import lombok.experimental.UtilityClass;
import mipt.app.secondmemory.dto.directory.BucketDto;
import mipt.app.secondmemory.entity.BucketEntity;

@UtilityClass
public class BucketMapper {
  public static BucketDto toBucketDto(BucketEntity bucketEntity) {
    return BucketDto.builder()
        .rootFolderId(bucketEntity.getRootFolderId())
        .id(bucketEntity.getId())
        .name(bucketEntity.getName())
        .build();
  }

  public static String toBucketName(BucketEntity bucketEntity) {
    return bucketEntity.getName();
  }
}
