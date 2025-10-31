package at.fhtw.paperlessrest.domain.model;

import java.util.List;
import java.util.Optional;

public interface FileMetaDataRepository {
    FileMetaData save(FileMetaData fileMetaData);
    List<FileMetaData> findAll();
    Optional<FileMetaData> findFileMetaDataByFileToken(FileToken token);
    void deleteByFileToken(FileToken fileToken);
}
