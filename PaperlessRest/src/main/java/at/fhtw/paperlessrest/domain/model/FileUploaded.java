package at.fhtw.paperlessrest.domain.model;

import lombok.Builder;

import java.time.LocalDateTime;

public record FileUploaded(
        byte[] file,
        LocalDateTime occurredOn
) {
    @Builder
    public FileUploaded(byte[] file) {
        this(file, LocalDateTime.now());
    }
}
