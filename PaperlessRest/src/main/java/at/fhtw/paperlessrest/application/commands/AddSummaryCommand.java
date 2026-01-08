package at.fhtw.paperlessrest.application.commands;

import lombok.Builder;

import java.util.UUID;

@Builder
public record AddSummaryCommand(String summary, UUID userToken, UUID fileToken) {
}
