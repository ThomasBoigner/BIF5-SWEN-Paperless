package at.fhtw.paperlessrest.application.commands;

import lombok.Builder;
import org.jspecify.annotations.Nullable;

import java.util.UUID;

@Builder
public record RegisterUserCommand(UUID token, @Nullable String username) { }
