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
import java.util.Objects;

@Slf4j
@Getter
@ToString
@Entity
public class FileMetaData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Nullable
    private Long id;
    @Embedded
    private FileToken fileToken;
    private LocalDateTime creationDate;
    @Embedded
    private FileName fileName;
    @Setter
    private int fileSize;
    @Nullable
    private String description;

    @Nullable
    private String fullText;
    @Nullable
    private String summary;

    public FileMetaData() {
        fileToken = new FileToken();
        creationDate = LocalDateTime.MIN;
        fileName = new FileName("Empty", "jpg");
        fileSize = 0;
    }

    @Builder
    public FileMetaData(FileName fileName, int fileSize, @Nullable String description) {
        this.setFileToken(new FileToken());
        this.setCreationDate(LocalDateTime.now());
        this.setFileName(fileName);
        this.setFileSize(fileSize);
        this.setDescription(description);
        log.debug("FileMetaData {} created", this);
    }

    public void setId(@Nullable Long id) {
        this.id = id;
    }

    public final void setFileToken(@Nullable FileToken fileToken) {
        Objects.requireNonNull(fileToken,  "fileToken must not be null!");
        this.fileToken = fileToken;
    }

    public final void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public final void setFileName(FileName fileName) {
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
        if (o == null || getClass() != o.getClass()) return false;
        FileMetaData that = (FileMetaData) o;
        return Objects.equals(fileToken, that.fileToken);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(fileToken);
    }
}
