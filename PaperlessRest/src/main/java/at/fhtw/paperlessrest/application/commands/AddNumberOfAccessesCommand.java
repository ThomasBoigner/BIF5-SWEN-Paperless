package at.fhtw.paperlessrest.application.commands;

import lombok.Builder;

import java.util.UUID;

@Builder
public record AddNumberOfAccessesCommand(int numberOfAccesses, UUID userToken, UUID fileToken) {
}
