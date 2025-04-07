package mipt.app.secondmemory.repository.tag;

import mipt.app.secondmemory.entity.TagEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TagsJpaRepository extends JpaRepository<TagEntity, Long> {}
