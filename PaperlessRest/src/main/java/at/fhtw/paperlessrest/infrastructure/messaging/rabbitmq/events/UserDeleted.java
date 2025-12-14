package at.fhtw.paperlessrest.infrastructure.messaging.rabbitmq.events;

import lombok.Builder;

import java.util.UUID;

@Builder
public record UserDeleted(UUID userId) { }
