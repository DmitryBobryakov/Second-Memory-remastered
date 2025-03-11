package mipt.app.secondmemory.repository;

import mipt.app.secondmemory.dto.FileInfoResponse;
import mipt.app.secondmemory.entity.File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FilesRepository extends JpaRepository<File, Long> {

  FileInfoResponse findByFileId(long fileId);

}
