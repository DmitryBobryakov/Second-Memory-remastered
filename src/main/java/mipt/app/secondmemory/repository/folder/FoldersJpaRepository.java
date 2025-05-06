package mipt.app.secondmemory.repository.folder;

import mipt.app.secondmemory.dto.directory.FolderDto;
import mipt.app.secondmemory.entity.FolderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FoldersJpaRepository extends JpaRepository<FolderEntity, Long> {
  @Query(
      value =
          """
                with recursive t as (
                    select id, parent_id, "name"
                    from folders f
                    where parent_id is null
                    union all
                    select f.id, f.parent_id,
                    case when t.name= '' then f.name else concat(t.name, '/', f.name) end
                    from t
                    join folders f on f.parent_id = t.id
                )
                select name
                from t
                where id = :nodeId;
              """,
      nativeQuery = true)
  String takePathToFolder(@Param("nodeId") Long nodeId);

  List<FolderEntity> findByParentId(Long parentId);
}
