package mipt.app.secondmemory.service.tag;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mipt.app.secondmemory.dto.tag.TagDto;
import mipt.app.secondmemory.entity.TagEntity;
import mipt.app.secondmemory.exception.tag.TagNotFoundException;
import mipt.app.secondmemory.mapper.TagMapper;
import mipt.app.secondmemory.repository.tag.TagsJpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class TagsService {
  private final TagsJpaRepository tagsRepository;
  private final TagMapper tagMapper;

  public TagDto get(Long tagId) throws TagNotFoundException {
    log.info("Функция по взятию тега вызвана в сервисе");
    TagEntity tagEntity = tagsRepository.findById(tagId).orElseThrow(TagNotFoundException::new);
    return tagMapper.toDto(tagEntity);
  }

  @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
  public TagDto create(TagEntity tagEntity) {
    log.info("Функция по созданию тега вызвана в сервисе");
    tagsRepository.save(tagEntity);
    return tagMapper.toDto(tagEntity);
  }

  @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.REPEATABLE_READ)
  public TagDto replace(Long tagId, TagEntity newTagEntity) throws TagNotFoundException {
    log.info("Функция по замене тега вызвана в сервисе");
    TagEntity tagEntity = tagsRepository.findById(tagId).orElseThrow(TagNotFoundException::new);
    tagEntity.setName(newTagEntity.getName());
    tagsRepository.save(tagEntity);
    return tagMapper.toDto(tagEntity);
  }

  @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.REPEATABLE_READ)
  public TagDto delete(Long tagId) throws TagNotFoundException {
    log.info("Функция по удалению тега вызвана в сервисе");
    TagEntity tagEntity = tagsRepository.findById(tagId).orElseThrow(TagNotFoundException::new);
    tagsRepository.deleteById(tagId);
    return tagMapper.toDto(tagEntity);
  }
}
