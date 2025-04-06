package mipt.app.secondmemory.repository;

import mipt.app.secondmemory.entity.FileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FilesJpaRepository extends JpaRepository<FileEntity, Long> {}
