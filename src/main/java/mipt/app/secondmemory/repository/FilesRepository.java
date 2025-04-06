package mipt.app.secondmemory.repository;

import java.util.List;
import mipt.app.secondmemory.entity.File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FilesRepository extends JpaRepository<File, Long> {
  List<File> findByNameLike(String name);
}
