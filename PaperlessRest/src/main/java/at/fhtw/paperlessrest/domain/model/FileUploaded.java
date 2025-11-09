package at.fhtw.paperlessrest.domain.model;

import lombok.Builder;

import java.time.LocalDateTime;

public record FileUploaded(
        byte[] file,
        FileToken fileToken,
        LocalDateTime occurredOn
) {
    @Builder
    public FileUploaded(byte[] file, FileToken fileToken) {
        this(file, fileToken, LocalDateTime.now());
    }
}
