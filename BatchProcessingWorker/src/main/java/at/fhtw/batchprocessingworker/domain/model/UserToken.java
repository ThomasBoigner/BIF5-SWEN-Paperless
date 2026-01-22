package at.fhtw.batchprocessingworker.domain.model;

import lombok.Builder;

import java.util.UUID;

@Builder
public record UserToken(UUID token) { }
