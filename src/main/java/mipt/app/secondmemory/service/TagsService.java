package mipt.app.secondmemory.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mipt.app.secondmemory.dto.tag.FileTagDto;
import mipt.app.secondmemory.dto.tag.TagDto;
import mipt.app.secondmemory.entity.CrsFileTagEntity;
import mipt.app.secondmemory.entity.FileEntity;
import mipt.app.secondmemory.entity.TagEntity;
import mipt.app.secondmemory.exception.file.FileNotFoundException;
import mipt.app.secondmemory.exception.tag.TagNotFoundException;
import mipt.app.secondmemory.mapper.TagsMapper;
import mipt.app.secondmemory.repository.CrsFilesTagsRepository;
import mipt.app.secondmemory.repository.FilesRepository;
import mipt.app.secondmemory.repository.TagsJpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class TagsService {
  private final TagsJpaRepository tagsRepository;
  private final CrsFilesTagsRepository crsFilesTagsRepository;
  private final FilesRepository filesRepository;

  public TagDto getTag(Long tagId) throws TagNotFoundException {
    log.debug("Функция по взятию тега c id: {} вызвана в сервисе", tagId);
    TagEntity tagEntity = tagsRepository.findById(tagId).orElseThrow(TagNotFoundException::new);
    return TagsMapper.toDto(tagEntity);
  }

  @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
  public FileTagDto deleteTagWithFileId(Long tagId, Long fileId)
      throws FileNotFoundException, TagNotFoundException {
    log.debug(
        "Функция по удалению тега у файла вызвана в сервисе. tagId: {}, fileId: {}", tagId, fileId);
    if (!filesRepository.existsById(fileId)) {
      throw new FileNotFoundException("File does not exists with fileId: " + fileId);
    }
    if (!tagsRepository.existsById(tagId)) {
      throw new TagNotFoundException("Tag does not exists with tagId: " + tagId);
    }
    CrsFileTagEntity crsFileTag =
        crsFilesTagsRepository
            .findByTagIdAndFileId(tagId, fileId)
            .orElseThrow(TagNotFoundException::new);
    crsFilesTagsRepository.delete(crsFileTag);
    FileEntity fileEntity =
        filesRepository.findById(fileId).orElseThrow(FileNotFoundException::new);
    fileEntity.setLastModifiedTs(Timestamp.from(Instant.now()));
    filesRepository.save(fileEntity);
    return new FileTagDto(tagId, fileId);
  }

  @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
  public FileTagDto addTagToFile(Long fileId, String tagName) throws FileNotFoundException {
    log.debug("Функция по удалению тега вызвана в сервисе");
    if (!filesRepository.existsById(fileId)) {
      throw new FileNotFoundException("File does not exists with fileId: " + fileId);
    }
    TagEntity tagEntity;
    if (!tagsRepository.existsByName(tagName)) {
      tagEntity = TagEntity.builder().name(tagName).build();
      tagsRepository.save(tagEntity);
    } else {
      tagEntity = tagsRepository.findByName(tagName);
    }
    CrsFileTagEntity crsFileTag = new CrsFileTagEntity();
    crsFileTag.setFileId(fileId);
    crsFileTag.setTagId(tagEntity.getId());
    crsFilesTagsRepository.save(crsFileTag);
    FileEntity fileEntity =
        filesRepository.findById(fileId).orElseThrow(FileNotFoundException::new);
    fileEntity.setLastModifiedTs(Timestamp.from(Instant.now()));
    filesRepository.save(fileEntity);
    return new FileTagDto(tagEntity.getId(), fileId);
  }

  @Transactional(readOnly = true)
  public List<TagDto> getAllTagsByFileId(Long fileId) {
    return tagsRepository.findAllTagsByFileId(fileId).stream().map(TagsMapper::toDto).toList();
  }
}
