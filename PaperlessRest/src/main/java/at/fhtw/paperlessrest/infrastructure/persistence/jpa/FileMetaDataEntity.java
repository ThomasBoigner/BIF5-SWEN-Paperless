package at.fhtw.paperlessrest.infrastructure.persistence.jpa;

import at.fhtw.paperlessrest.domain.model.FileMetaData;
import at.fhtw.paperlessrest.domain.model.FileToken;
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

    @Nullable
    private String fullText;
    @Nullable
    private String summary;

    public FileMetaDataEntity() {
        this.fileToken = UUID.randomUUID();
        this.creationDate = LocalDateTime.MIN;
        this.fileName = "";
    }

    public FileMetaDataEntity(FileMetaData fileMetaData) {
        this.fileToken = fileMetaData.getFileToken().token();
        this.creationDate = fileMetaData.getCreationDate();
        this.fileName = fileMetaData.getFileName();
        this.fileSize = fileMetaData.getFileSize();
        this.description = fileMetaData.getDescription();
        this.fullText = fileMetaData.getFullText();
        this.summary = fileMetaData.getSummary();
    }

    public FileMetaData toFileMetaData() {
        return new FileMetaData(
                new FileToken(fileToken),
                this.creationDate,
                this.fileName,
                this.fileSize,
                this.description,
                this.fullText,
                this.summary
        );
    }
}
