package at.fhtw.ocrworker.infrastructure.messaging.rabbitmq.events;

import java.util.UUID;

public record UserToken(UUID token) {
}
