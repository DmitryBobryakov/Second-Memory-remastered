package mipt.app.secondmemory.repository;

import mipt.app.secondmemory.entity.CrsFileTagEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface CrsFilesTagsRepository extends JpaRepository<CrsFileTagEntity, Long> {

  @Query(
      value = "SELECT * FROM files_tags WHERE file_id = :fileId and tag_id = :tagId",
      nativeQuery = true)
  Optional<CrsFileTagEntity> findByTagIdAndFileId(
      @Param("tagId") Long tagId, @Param("fileId") Long fileId);
}
