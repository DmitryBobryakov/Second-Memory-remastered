package mipt.app.secondmemory.controller.folder;

import io.minio.errors.ErrorResponseException;
import io.minio.errors.InsufficientDataException;
import io.minio.errors.InternalException;
import io.minio.errors.InvalidResponseException;
import io.minio.errors.ServerException;
import io.minio.errors.XmlParserException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mipt.app.secondmemory.dto.directory.FolderDto;
import mipt.app.secondmemory.exception.directory.BucketNotFoundException;
import mipt.app.secondmemory.exception.directory.NoSuchDirectoryException;
import mipt.app.secondmemory.service.FoldersService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class FoldersControllerImpl implements FoldersController {

  private final FoldersService foldersService;

  public ResponseEntity<FolderDto> createFolder(String folderName, Long bucketId, Long parentId)
      throws ServerException,
          InsufficientDataException,
          BucketNotFoundException,
          ErrorResponseException,
          IOException,
          NoSuchAlgorithmException,
          InvalidKeyException,
          InvalidResponseException,
          XmlParserException,
          InternalException {
    return ResponseEntity.ok(foldersService.createFolder(folderName, bucketId, parentId));
  }

  public ResponseEntity<FolderDto> deleteFolder(Long folderId)
      throws ServerException,
          InsufficientDataException,
          BucketNotFoundException,
          ErrorResponseException,
          IOException,
          NoSuchAlgorithmException,
          InvalidKeyException,
          InvalidResponseException,
          XmlParserException,
          InternalException,
          NoSuchDirectoryException {
    return ResponseEntity.ok(foldersService.deleteFolder(folderId));
  }
}
