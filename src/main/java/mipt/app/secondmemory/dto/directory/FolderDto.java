package mipt.app.secondmemory.dto.directory;

import lombok.Builder;

@Builder(toBuilder = true)
public record FolderDto(Long folderId, Long bucketId, Long parentId, String name) {}
