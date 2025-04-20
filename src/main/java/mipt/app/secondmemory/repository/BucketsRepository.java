package mipt.app.secondmemory.repository;

import mipt.app.secondmemory.entity.BucketEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BucketsRepository extends JpaRepository<BucketEntity, Long> {
  @Query("select id from BucketEntity where name= :bucketName")
  Long findByName(@Param("bucketName") String name);
}
