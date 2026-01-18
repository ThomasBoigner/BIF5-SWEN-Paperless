package at.fhtw.paperlessrest.infrastructure.persistence.jpa;

import at.fhtw.paperlessrest.domain.model.FileMetaData;
import at.fhtw.paperlessrest.domain.model.FileToken;
import at.fhtw.paperlessrest.infrastructure.persistence.elasticsearch.FileDocument;
import jakarta.persistence.*;
import lombok.Getter;
import org.jspecify.annotations.Nullable;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
public class FileMetaDataEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Nullable
    private Long id;
    private UUID fileToken;
    private LocalDateTime creationDate;
    private String fileName;
    private long fileSize;
    @Nullable
    private String description;

    public FileMetaDataEntity() {
        this.fileToken = UUID.randomUUID();
        this.creationDate = LocalDateTime.MIN;
        this.fileName = "";
    }

    public FileMetaDataEntity(FileMetaData fileMetaData) {
        this.id = fileMetaData.getId();
        this.fileToken = fileMetaData.getFileToken().token();
        this.creationDate = fileMetaData.getCreationDate();
        this.fileName = fileMetaData.getFileName();
        this.fileSize = fileMetaData.getFileSize();
        this.description = fileMetaData.getDescription();
    }

    public FileMetaData toFileMetaData(FileDocument fileDocument) {
        return new FileMetaData(
                this.id,
                new FileToken(fileToken),
                this.creationDate,
                this.fileName,
                this.fileSize,
                this.description,
                fileDocument.getFullText(),
                fileDocument.getSummary()
        );
    }
}
