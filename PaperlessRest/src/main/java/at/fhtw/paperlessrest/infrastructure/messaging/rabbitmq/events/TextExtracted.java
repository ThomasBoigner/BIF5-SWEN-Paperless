package at.fhtw.paperlessrest.infrastructure.messaging.rabbitmq.events;

import at.fhtw.paperlessrest.domain.model.UserToken;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record TextExtracted(
        String fullText,
        UserToken userToken,
        FileToken fileToken,
        LocalDateTime occurredOn
) { }
