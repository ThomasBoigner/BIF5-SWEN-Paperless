package at.fhtw.ocrworker.domain.model;

import lombok.Builder;

import java.time.LocalDateTime;

public record TextExtracted(
        String fullText,
        UserToken userToken,
        FileToken fileToken,
        LocalDateTime occurredOn
) {
    @Builder
    public TextExtracted(String fullText, UserToken userToken, FileToken fileToken) {
        this(fullText, userToken, fileToken, LocalDateTime.now());
    }
}
