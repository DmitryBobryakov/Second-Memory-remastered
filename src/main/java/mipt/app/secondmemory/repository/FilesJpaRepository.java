package mipt.app.secondmemory.repository;

import mipt.app.secondmemory.entity.File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface FilesJpaRepository extends JpaRepository<File, Long> {
  @Query("SELECT id FROM File")
  List<Long> findAllId();
}
