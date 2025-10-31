package at.fhtw.paperlessrest.infrastructure.persistence.jpa;

import at.fhtw.paperlessrest.domain.model.FileMetaData;
import at.fhtw.paperlessrest.domain.model.FileMetaDataRepository;
import at.fhtw.paperlessrest.domain.model.FileToken;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor

@Repository
public class JpaFileMetaDataRepository implements FileMetaDataRepository {
    private final FileMetaDataEntityRepository fileMetaDataEntityRepository;

    @Override
    public FileMetaData save(FileMetaData fileMetaData) {
        FileMetaDataEntity fileMetaDataEntity = new FileMetaDataEntity(fileMetaData);
        this.fileMetaDataEntityRepository.save(fileMetaDataEntity);
        return fileMetaData;
    }

    @Override
    public List<FileMetaData> findAll() {
        return fileMetaDataEntityRepository.findAll().stream().map(FileMetaDataEntity::toFileMetaData).toList();
    }

    @Override
    public Optional<FileMetaData> findFileMetaDataByFileToken(FileToken token) {
        return fileMetaDataEntityRepository.findFileMetaDataByFileToken(token.token()).map(FileMetaDataEntity::toFileMetaData);
    }

    @Override
    public void deleteByFileToken(FileToken fileToken) {
        fileMetaDataEntityRepository.deleteByFileToken(fileToken.token());
    }
}
