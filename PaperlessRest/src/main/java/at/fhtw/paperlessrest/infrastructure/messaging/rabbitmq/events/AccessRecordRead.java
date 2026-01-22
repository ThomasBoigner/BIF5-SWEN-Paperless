package at.fhtw.paperlessrest.infrastructure.messaging.rabbitmq.events;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record AccessRecordRead(
        UserToken userToken,
        FileToken fileToken,
        int numberOfAccesses,
        LocalDateTime occurredOn
) { }
