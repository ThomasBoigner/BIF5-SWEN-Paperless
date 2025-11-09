package at.fhtw.paperlessrest.infrastructure.messaging.rabbitmq.events;

import java.time.LocalDateTime;

public record TextExtracted(
        String fullText,
        FileToken fileToken,
        LocalDateTime occurredOn
) { }
