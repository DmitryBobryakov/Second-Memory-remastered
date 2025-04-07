package mipt.app.secondmemory.dto.tag;

import lombok.Builder;

@Builder(toBuilder = true)
public record TagDto(Long id, String name) {}
