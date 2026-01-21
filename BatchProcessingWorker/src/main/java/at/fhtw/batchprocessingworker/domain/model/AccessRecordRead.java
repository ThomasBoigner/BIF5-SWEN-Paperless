package at.fhtw.batchprocessingworker.domain.model;

import lombok.Builder;

import java.time.LocalDateTime;

public record AccessRecordRead(
        UserToken userToken,
        FileToken fileToken,
        int numberOfAccesses,
        LocalDateTime occurredOn
) {
    @Builder
    public AccessRecordRead(UserToken userToken, FileToken fileToken, int numberOfAccesses) {
        this(userToken, fileToken, numberOfAccesses, LocalDateTime.now());
    }
}
