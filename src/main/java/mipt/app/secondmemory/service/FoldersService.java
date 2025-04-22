package mipt.app.secondmemory.service;

import io.minio.errors.ErrorResponseException;
import io.minio.errors.InsufficientDataException;
import io.minio.errors.InternalException;
import io.minio.errors.InvalidResponseException;
import io.minio.errors.ServerException;
import io.minio.errors.XmlParserException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mipt.app.secondmemory.dto.directory.FolderDto;
import mipt.app.secondmemory.entity.FolderEntity;
import mipt.app.secondmemory.exception.directory.BucketNotFoundException;
import mipt.app.secondmemory.exception.directory.NoSuchDirectoryException;
import mipt.app.secondmemory.mapper.FoldersMapper;
import mipt.app.secondmemory.repository.bucket.BucketsJpaRepository;
import mipt.app.secondmemory.repository.folder.FoldersJpaRepository;
import mipt.app.secondmemory.repository.folder.FoldersS3Repository;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Service
@RequiredArgsConstructor
@Slf4j
public class FoldersService {

  private final FoldersJpaRepository foldersJpaRepository;
  private final FoldersS3Repository foldersS3Repository;
  private final BucketsJpaRepository bucketsJpaRepository;

  public FolderDto createFolder(String folderName, Long bucketId, Long parentId)
      throws ServerException,
          InsufficientDataException,
          ErrorResponseException,
          IOException,
          NoSuchAlgorithmException,
          InvalidKeyException,
          InvalidResponseException,
          XmlParserException,
          InternalException,
          BucketNotFoundException {

    String bucketName =
        bucketsJpaRepository.findById(bucketId).orElseThrow(BucketNotFoundException::new).getName();
    String pathToFolder = "%s/".formatted(foldersJpaRepository.takePathToFolder(parentId));
    foldersS3Repository.createFolder(folderName, pathToFolder, bucketName);

    FolderEntity folderEntity =
        FolderEntity.builder().bucketId(bucketId).name(folderName).parentId(parentId).build();
    foldersJpaRepository.save(folderEntity);

    return FoldersMapper.toFolderDto(folderEntity);
  }

  public FolderDto deleteFolder(Long folderId)
      throws NoSuchDirectoryException,
          BucketNotFoundException,
          ServerException,
          InsufficientDataException,
          ErrorResponseException,
          IOException,
          NoSuchAlgorithmException,
          InvalidKeyException,
          InvalidResponseException,
          XmlParserException,
          InternalException {

    FolderEntity folderEntity =
        foldersJpaRepository.findById(folderId).orElseThrow(NoSuchDirectoryException::new);
    String bucketName =
        bucketsJpaRepository
            .findById(folderEntity.getBucketId())
            .orElseThrow(BucketNotFoundException::new)
            .getName();
    String folderPrefix = "%s/".formatted(foldersJpaRepository.takePathToFolder(folderId));
    foldersJpaRepository.delete(folderEntity);
    foldersS3Repository.deleteFolder(bucketName, folderPrefix);
    return FoldersMapper.toFolderDto(folderEntity);
  }
}
