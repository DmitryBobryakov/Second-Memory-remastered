package mipt.app.secondmemory.dto.file;

import lombok.Builder;

import java.sql.Timestamp;

@Builder(toBuilder = true)
public record FileInfoResponse(
    Long fileId,
    String fileName,
    long fileCapacity,
    Long fileOwnerId,
    Timestamp fileCreationDate,
    Timestamp fileLastModifiedDate,
    Long bucketId) {}
