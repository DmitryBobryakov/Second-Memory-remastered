package mipt.app.secondmemory.repository;

import java.util.Optional;
import mipt.app.secondmemory.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RolesRepository extends JpaRepository<Role, Long> {
  Optional<Role> findByUserIdAndFileId(Long userId, Long fileId);
}
