package mipt.app.secondmemory.repository;

import java.util.Optional;
import mipt.app.secondmemory.entity.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SessionsRepository extends JpaRepository<Session, Long> {
  Optional<Session> findByUserId(Long id);

  Optional<Session> findByCookie(String cookieValue);
}
