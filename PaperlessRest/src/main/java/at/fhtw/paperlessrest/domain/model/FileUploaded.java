package at.fhtw.paperlessrest.domain.model;

import lombok.Builder;

import java.time.LocalDateTime;

public record FileUploaded(
        FileToken fileToken,
        LocalDateTime occurredOn
) {
    @Builder
    public FileUploaded(FileToken fileToken) {
        this(fileToken, LocalDateTime.now());
    }
}
