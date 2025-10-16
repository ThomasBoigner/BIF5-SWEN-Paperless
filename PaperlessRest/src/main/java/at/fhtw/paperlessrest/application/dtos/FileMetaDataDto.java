package at.fhtw.paperlessrest.application.dtos;

import at.fhtw.paperlessrest.domain.model.FileMetaData;
import org.jspecify.annotations.Nullable;

import java.time.LocalDateTime;
import java.util.UUID;

public record FileMetaDataDto(
        UUID fileToken,
        LocalDateTime creationDate,
        String fileName,
        long fileSize,
        @Nullable
        String description,
        @Nullable
        String fullText,
        @Nullable
        String summary
) {
    public FileMetaDataDto(FileMetaData fileMetaData) {
        this(
                fileMetaData.getFileToken().token(),
                fileMetaData.getCreationDate(),
                fileMetaData.getFileName(),
                fileMetaData.getFileSize(),
                fileMetaData.getDescription(),
                fileMetaData.getFullText(),
                fileMetaData.getSummary()
        );
    }
}
