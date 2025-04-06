package mipt.app.secondmemory.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mipt.app.secondmemory.dto.TagDto;
import mipt.app.secondmemory.entity.Tag;
import mipt.app.secondmemory.exception.TagNotFoundException;
import mipt.app.secondmemory.mapper.TagMapper;
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
  private final TagMapper tagMapper;

  public TagDto get(Long tagId) throws TagNotFoundException {
    log.info("Функция по взятию тега вызвана в сервисе");
    Tag tag = tagsRepository.findById(tagId).orElseThrow(TagNotFoundException::new);
    return tagMapper.toDto(tag);
  }

  @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
  public TagDto create(Tag tag) {
    log.info("Функция по созданию тега вызвана в сервисе");
    tagsRepository.save(tag);
    return tagMapper.toDto(tag);
  }

  @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.REPEATABLE_READ)
  public TagDto replace(Long tagId, Tag newTag) throws TagNotFoundException {
    log.info("Функция по замене тега вызвана в сервисе");
    Tag tag = tagsRepository.findById(tagId).orElseThrow(TagNotFoundException::new);
    tag.setName(newTag.getName());
    tagsRepository.save(tag);
    return tagMapper.toDto(tag);
  }

  @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.REPEATABLE_READ)
  public TagDto delete(Long tagId) throws TagNotFoundException {
    log.info("Функция по удалению тега вызвана в сервисе");
    Tag tag = tagsRepository.findById(tagId).orElseThrow(TagNotFoundException::new);
    tagsRepository.deleteById(tagId);
    return tagMapper.toDto(tag);
  }
}
