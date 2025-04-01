package mipt.app.secondmemory.dto;

import java.sql.Timestamp;

public record FileInfoResponse(long fileId, String fileName, long fileOwnerId,
                               Timestamp fileCreationDate, Timestamp fileLastModifiedDate,
                               long fileAccessLevelId) {}
