package at.fhtw.batchprocessingworker.application.commands;

import lombok.Builder;

import java.util.UUID;

@Builder
public record CreateAccessRecordCommand(UUID userToken, UUID fileToken) { }
