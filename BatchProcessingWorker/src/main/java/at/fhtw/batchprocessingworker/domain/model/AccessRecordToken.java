package at.fhtw.batchprocessingworker.domain.model;

import lombok.Builder;

import java.util.UUID;

@Builder
public record AccessRecordToken(UUID token) {
    public AccessRecordToken() {
        this(UUID.randomUUID());
    }
}
