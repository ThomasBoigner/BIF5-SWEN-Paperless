package at.fhtw.genaiworker.domain.model;

import lombok.Builder;

import java.time.LocalDateTime;

public record SummaryCreated(
       String summary,
       UserToken userToken,
       FileToken fileToken,
       LocalDateTime occurredOn
) {
    @Builder
    public SummaryCreated(String summary, UserToken userToken, FileToken fileToken) {
        this(summary, userToken, fileToken, LocalDateTime.now());
    }
}
