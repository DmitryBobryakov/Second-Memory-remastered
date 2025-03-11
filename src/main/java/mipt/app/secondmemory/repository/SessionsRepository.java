package mipt.app.secondmemory.repository;

import mipt.app.secondmemory.entity.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SessionsRepository extends JpaRepository<Session, Long> {
  Session findByUuid(Long uuid);
}
