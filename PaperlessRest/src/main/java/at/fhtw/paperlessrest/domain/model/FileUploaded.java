package at.fhtw.paperlessrest.domain.model;

import lombok.Builder;

import java.time.LocalDateTime;

public record FileUploaded(
        UserToken userToken,
        FileToken fileToken,
        LocalDateTime occurredOn
) {
    @Builder
    public FileUploaded(UserToken userToken, FileToken fileToken) {
        this(userToken, fileToken, LocalDateTime.now());
    }
}
