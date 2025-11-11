package at.fhtw.ocrworker.domain.model;

import lombok.Builder;

import java.time.LocalDateTime;

public record TextExtracted(
        String fullText,
        FileToken fileToken,
        LocalDateTime occurredOn
) {
    @Builder
    public TextExtracted(String fullText, FileToken fileToken) {
        this(fullText, fileToken, LocalDateTime.now());
    }
}
