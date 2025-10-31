package at.fhtw.paperlessrest.infrastructure.persistence.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface FileMetaDataEntityRepository extends JpaRepository<FileMetaDataEntity, Long> {
    Optional<FileMetaDataEntity> findFileMetaDataByFileToken(UUID token);
    void deleteByFileToken(UUID fileToken);
}
