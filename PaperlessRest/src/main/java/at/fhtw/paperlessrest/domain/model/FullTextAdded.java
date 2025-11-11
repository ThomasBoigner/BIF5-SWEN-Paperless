package at.fhtw.paperlessrest.domain.model;

import lombok.Builder;

import java.time.LocalDateTime;

public record FullTextAdded(String fullText, FileToken fileToken, LocalDateTime createdAt) {
    @Builder
    public FullTextAdded(String fullText, FileToken fileToken) {
        this(fullText, fileToken, LocalDateTime.now());
    }
}
