package mipt.app.secondmemory.repository;

import java.util.Optional;
import mipt.app.secondmemory.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersRepository extends JpaRepository<User, Long> {
  Optional<User> findByUuid(Long uuid);

  Optional<User> findByEmail(String email);
}
