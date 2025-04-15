package mipt.app.secondmemory.repository;

import mipt.app.secondmemory.entity.BucketEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BucketsJpaRepository extends JpaRepository<BucketEntity, Long> {

    @Query("select b.name from BucketEntity as b")
    List<BucketEntity> findBucketsNames();
}
