package mipt.app.secondmemory.controller.folder;

import io.minio.errors.ErrorResponseException;
import io.minio.errors.InsufficientDataException;
import io.minio.errors.InternalException;
import io.minio.errors.InvalidResponseException;
import io.minio.errors.ServerException;
import io.minio.errors.XmlParserException;
import io.swagger.v3.oas.annotations.tags.Tag;
import mipt.app.secondmemory.dto.directory.FolderDto;
import mipt.app.secondmemory.exception.directory.BucketNotFoundException;
import mipt.app.secondmemory.exception.directory.NoSuchDirectoryException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Tag(name = "Folders API", description = "Управление дочерними папками")
public interface FoldersController {

  @PostMapping("folders/create/{bucketId}/{parentId}")
  ResponseEntity<FolderDto> createFolder(
      @RequestParam(name = "folderName") String folderName,
      @PathVariable(name = "bucketId") Long bucketId,
      @PathVariable(name = "parentId") Long parentId)
      throws ServerException,
          InsufficientDataException,
          BucketNotFoundException,
          ErrorResponseException,
          IOException,
          NoSuchAlgorithmException,
          InvalidKeyException,
          InvalidResponseException,
          XmlParserException,
          InternalException;

  @DeleteMapping("folders/delete/{folderId}")
  ResponseEntity<FolderDto> deleteFolder(@PathVariable("folderId") Long folderId)
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
          NoSuchDirectoryException;
}
