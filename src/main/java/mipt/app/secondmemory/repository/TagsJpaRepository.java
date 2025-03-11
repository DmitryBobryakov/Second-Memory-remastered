package mipt.app.secondmemory.repository;

import mipt.app.secondmemory.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface TagsJpaRepository extends JpaRepository<Tag, Long> {
}
