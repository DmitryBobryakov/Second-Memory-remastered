package mipt.app.secondmemory.dto.file;

import java.sql.Timestamp;
import lombok.Builder;

@Builder(toBuilder = true)
public record FileInfoResponse(
    Long fileId,
    String fileName,
    long fileCapacity,
    Long fileOwnerId,
    Timestamp fileCreationDate,
    Timestamp fileLastModifiedDate,
    Long bucketId) {}
