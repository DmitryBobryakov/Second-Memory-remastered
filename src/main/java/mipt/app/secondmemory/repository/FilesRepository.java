package mipt.app.secondmemory.repository;

import java.util.List;
import java.util.Optional;
import mipt.app.secondmemory.entity.FileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FilesRepository extends JpaRepository<FileEntity, Long> {

  List<FileEntity> findByNameLike(String name);

  Optional<FileEntity> findById(long fileId);

  List<FileEntity> findByFolderId(Long folderId);
}
