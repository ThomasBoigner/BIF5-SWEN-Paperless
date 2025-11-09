package at.fhtw.paperlessrest.infrastructure.messaging.rabbitmq.events;

import java.util.UUID;

public record FileToken(UUID token) {
}
