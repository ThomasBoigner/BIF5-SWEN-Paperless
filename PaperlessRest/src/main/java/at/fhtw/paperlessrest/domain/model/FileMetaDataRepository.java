package at.fhtw.paperlessrest.domain.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FileMetaDataRepository extends JpaRepository<FileMetaData, Long> {
    Optional<FileMetaData> findFileMetaDataByFileToken(FileToken token);
    void deleteFileMetaDataByFileToken(FileToken token);

    void deleteByFileToken(FileToken fileToken);
}
