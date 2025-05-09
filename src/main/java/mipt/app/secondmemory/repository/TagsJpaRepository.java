package mipt.app.secondmemory.repository;

import jakarta.validation.constraints.NotNull;
import mipt.app.secondmemory.entity.TagEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TagsJpaRepository extends JpaRepository<TagEntity, Long> {

  @Query(
      value =
          "SELECT * FROM tags WHERE id in (select tag_id from files_tags where file_id= :fileId)",
      nativeQuery = true)
  List<TagEntity> findAllTagsByFileId(@Param("fileId") Long fileId);

  boolean existsByName(@NotNull(message = "Tag name have to be filled") String name);

  TagEntity findByName(String name);
}
