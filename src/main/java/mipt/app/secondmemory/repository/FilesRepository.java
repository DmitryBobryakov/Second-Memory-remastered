package mipt.app.secondmemory.repository;

import java.util.Optional;
import mipt.app.secondmemory.entity.FileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FilesRepository extends JpaRepository<FileEntity, Long> {

  Optional<FileEntity> findById(long fileId);

}
