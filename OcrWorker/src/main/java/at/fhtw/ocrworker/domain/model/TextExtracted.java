package at.fhtw.ocrworker.domain.model;

import lombok.Builder;

import java.time.LocalDateTime;

public record TextExtracted(
        String fullText,
        LocalDateTime occurredOn
) {
    @Builder
    public TextExtracted(String fullText) {
        this(fullText, LocalDateTime.now());
    }
}
