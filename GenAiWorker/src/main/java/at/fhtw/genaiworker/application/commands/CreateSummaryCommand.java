package at.fhtw.genaiworker.application.commands;

import lombok.Builder;

import java.util.UUID;

@Builder
public record CreateSummaryCommand(UUID userToken, UUID fileToken, String fullText) {
}
