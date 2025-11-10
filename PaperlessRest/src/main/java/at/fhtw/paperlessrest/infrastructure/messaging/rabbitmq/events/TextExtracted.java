package at.fhtw.paperlessrest.infrastructure.messaging.rabbitmq.events;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record TextExtracted(
        String fullText,
        FileToken fileToken,
        LocalDateTime occurredOn
) { }
