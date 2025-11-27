package at.fhtw.paperlessrest.domain.model;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.Nullable;
import org.springframework.util.Assert;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
@Getter
public class FileMetaData {
    @Nullable
    private Long id;
    private FileToken fileToken;
    private LocalDateTime creationDate;
    private String fileName;
    @Setter
    private long fileSize;
    @Nullable
    private String description;

    @Nullable
    private String fullText;
    @Nullable
    private String summary;

    private final List<FileUploaded> fileUploadedEvents;
    private final List<FullTextAdded> fullTextAddedEvents;

    @Builder
    public FileMetaData(@Nullable String fileName, long fileSize, @Nullable String description) {
        this.setFileToken(new FileToken());
        this.setCreationDate(LocalDateTime.now(ZoneId.systemDefault()));
        this.setFileName(fileName);
        this.setFileSize(fileSize);
        this.setDescription(description);
        this.fileUploadedEvents = new ArrayList<>();
        this.fullTextAddedEvents = new ArrayList<>();
        log.debug("FileMetaData {} created", this);
        this.fileUploadedEvents.add(FileUploaded.builder()
                        .fileToken(fileToken)
                .build());
    }

    public FileMetaData(@Nullable Long id,
                        FileToken fileToken,
                        LocalDateTime creationDate,
                        String fileName,
                        long fileSize,
                        @Nullable String description,
                        @Nullable String fullText,
                        @Nullable String summary
    ) {
        this.id = id;
        this.setFileToken(fileToken);
        this.setCreationDate(creationDate);
        this.setFileName(fileName);
        this.setFileSize(fileSize);
        this.setDescription(description);
        this.setFullText(fullText);
        this.setSummary(summary);
        this.fileUploadedEvents = new ArrayList<>();
        this.fullTextAddedEvents = new ArrayList<>();
    }

    public void addFullText(String fullText) {
        this.setFullText(fullText);
        this.fullTextAddedEvents.add(FullTextAdded.builder()
                .fullText(fullText)
                .fileToken(this.fileToken)
                .build()
        );
    }

    private void setFileToken(@Nullable FileToken fileToken) {
        Objects.requireNonNull(fileToken,  "Token must not be null!");
        this.fileToken = fileToken;
    }

    private void setCreationDate(@Nullable LocalDateTime creationDate) {
        Objects.requireNonNull(creationDate, "Creation date must not be null!");
        this.creationDate = creationDate;
    }

    private void setFileName(@Nullable String fileName) {
        Objects.requireNonNull(fileName, "File name must not be null!");
        Assert.isTrue(!fileName.isBlank(), "File name must not be blank!");
        this.fileName = fileName;
    }

    public void setDescription(@Nullable String description) {
        if (description != null) {
            Assert.isTrue(!description.isBlank(), "Description must not be blank!");
        }
        this.description = description;
    }

    private void setFullText(@Nullable String fullText) {
        this.fullText = fullText;
    }

    private void setSummary(@Nullable String summary) {
        this.summary = summary;
    }

    @Override
    public String toString() {
        return "FileMetaData{" +
                "fileToken=" + fileToken +
                ", creationDate=" + creationDate +
                ", fileName='" + fileName + '\'' +
                ", fileSize=" + fileSize +
                ", description='" + description + '\'' +
                ", fullText='" + fullText + '\'' +
                ", summary='" + summary + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof FileMetaData that)) return false;
        return Objects.equals(fileToken, that.fileToken);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(fileToken);
    }
}
