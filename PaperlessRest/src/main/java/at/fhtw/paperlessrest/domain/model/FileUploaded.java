package at.fhtw.paperlessrest.domain.model;

import lombok.Builder;

import java.time.LocalDateTime;

public record FileUploaded(
        UserToken userToken,
        FileToken fileToken,
        String fileName,
        LocalDateTime occurredOn
) {
    @Builder
    public FileUploaded(UserToken userToken, FileToken fileToken, String fileName) {
        this(userToken, fileToken, fileName, LocalDateTime.now());
    }
}
