package mipt.app.secondmemory.dto;

import java.sql.Timestamp;

public record FileInfoResponse(
    Long fileId,
    String fileName,
    long fileCapacity,
    Long fileOwnerId,
    Timestamp fileCreationDate,
    Timestamp fileLastModifiedDate,
    Long bucketId) {}
