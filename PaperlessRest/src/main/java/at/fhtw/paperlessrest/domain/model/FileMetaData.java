package at.fhtw.paperlessrest.domain.model;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.Nullable;
import org.springframework.util.Assert;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Objects;

@Slf4j
@Getter
@ToString
public class FileMetaData {
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

    @Builder
    public FileMetaData(@Nullable String fileName, long fileSize, @Nullable String description) {
        this.setFileToken(new FileToken());
        this.setCreationDate(LocalDateTime.now(ZoneId.systemDefault()));
        this.setFileName(fileName);
        this.setFileSize(fileSize);
        this.setDescription(description);
        log.debug("FileMetaData {} created", this);
    }

    public FileMetaData(FileToken fileToken,
                        LocalDateTime creationDate,
                        String fileName,
                        long fileSize,
                        @Nullable String description,
                        @Nullable String fullText,
                        @Nullable String summary
    ) {
        this.setFileToken(fileToken);
        this.setCreationDate(creationDate);
        this.setFileName(fileName);
        this.setFileSize(fileSize);
        this.setDescription(description);
        this.setFullText(fullText);
        this.setSummary(summary);
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

    public void setFullText(@Nullable String fullText) {
        this.fullText = fullText;
    }

    public void setSummary(@Nullable String summary) {
        this.summary = summary;
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
