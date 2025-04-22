package mipt.app.secondmemory.mapper;

import lombok.experimental.UtilityClass;
import mipt.app.secondmemory.dto.directory.FolderDto;
import mipt.app.secondmemory.entity.FolderEntity;

@UtilityClass
public class FoldersMapper {
  public static FolderDto toFolderDto(FolderEntity folderEntity) {
    return FolderDto.builder()
        .bucketId(folderEntity.getBucketId())
        .parentId(folderEntity.getParentId())
        .name(folderEntity.getName())
        .build();
  }
}
