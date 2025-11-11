package at.fhtw.paperlessrest.application.commands;

import lombok.Builder;

import java.util.UUID;

@Builder
public record AddFullTextCommand(String FullText, UUID fileToken) {
}
