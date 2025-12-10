package at.fhtw.paperlessrest.domain.model;

import lombok.Builder;

import java.time.LocalDateTime;

public record FullTextAdded(String fullText, UserToken userToken, FileToken fileToken, LocalDateTime createdAt) {
    @Builder
    public FullTextAdded(String fullText, UserToken userToken, FileToken fileToken) {
        this(fullText, userToken, fileToken, LocalDateTime.now());
    }
}
