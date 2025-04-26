package mipt.app.secondmemory.dto.tag;

import lombok.Builder;

@Builder(toBuilder = true)
public record FileTagDto(Long tagId, Long fileId) {}
