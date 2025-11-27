package at.fhtw.paperlessrest.infrastructure.messaging.rabbitmq.events;

import lombok.Builder;

@Builder
public record UserRegisteredDetails(
        String username
) { }
