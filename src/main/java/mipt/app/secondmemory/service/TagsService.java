package mipt.app.secondmemory.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mipt.app.secondmemory.dto.tag.TagDto;
import mipt.app.secondmemory.entity.TagEntity;
import mipt.app.secondmemory.exception.tag.TagNotFoundException;
import mipt.app.secondmemory.mapper.TagsMapper;
import mipt.app.secondmemory.repository.TagsJpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class TagsService {
  private final TagsJpaRepository tagsRepository;

  public TagDto get(Long tagId) throws TagNotFoundException {
    log.debug("Функция по взятию тега вызвана в сервисе");
    TagEntity tagEntity = tagsRepository.findById(tagId).orElseThrow(TagNotFoundException::new);
    return TagsMapper.toDto(tagEntity);
  }

  @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
  public TagDto create(TagEntity tagEntity) {
    log.debug("Функция по созданию тега вызвана в сервисе");
    tagsRepository.save(tagEntity);
    return TagsMapper.toDto(tagEntity);
  }

  @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.REPEATABLE_READ)
  public TagDto replace(Long tagId, TagEntity newTagEntity) throws TagNotFoundException {
    log.debug("Функция по замене тега вызвана в сервисе");
    TagEntity tagEntity = tagsRepository.findById(tagId).orElseThrow(TagNotFoundException::new);
    tagEntity.setName(newTagEntity.getName());
    tagsRepository.save(tagEntity);
    return TagsMapper.toDto(tagEntity);
  }

  @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.REPEATABLE_READ)
  public TagDto delete(Long tagId) throws TagNotFoundException {
    log.debug("Функция по удалению тега вызвана в сервисе");
    TagEntity tagEntity = tagsRepository.findById(tagId).orElseThrow(TagNotFoundException::new);
    tagsRepository.deleteById(tagId);
    return TagsMapper.toDto(tagEntity);
  }
}
