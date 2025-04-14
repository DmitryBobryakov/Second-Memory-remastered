package mipt.app.secondmemory.repository;

import mipt.app.secondmemory.entity.BucketEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BucketsJpaRepository extends JpaRepository<BucketEntity, Long> {}
