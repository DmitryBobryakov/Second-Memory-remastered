package mipt.app.secondmemory.dto.directory;

import lombok.Builder;

@Builder(toBuilder = true)
public record BucketDto(Long id, String name, long rootFolderId) {}
