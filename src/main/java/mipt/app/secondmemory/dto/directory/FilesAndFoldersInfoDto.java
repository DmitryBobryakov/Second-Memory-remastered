package mipt.app.secondmemory.dto.directory;

import lombok.Builder;
import mipt.app.secondmemory.dto.file.FileInfoResponse;
import java.util.List;

@Builder
public record FilesAndFoldersInfoDto(List<FileInfoResponse> files, List<FolderDto> folders) {}
