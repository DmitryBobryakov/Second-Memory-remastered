package mipt.app.secondmemory.dto.file;

import lombok.Builder;
import mipt.app.secondmemory.dto.tag.TagDto;
import java.util.List;

@Builder(toBuilder = true)
public record FileDetailsDto(
    String bucketName,
    String fileName,
    String ownerName,
    String role,
    long size,
    long creationTs,
    long lastModifiedTs,
    List<TagDto> tags) {}
