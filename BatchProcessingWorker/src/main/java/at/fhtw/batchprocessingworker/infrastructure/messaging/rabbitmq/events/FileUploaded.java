package at.fhtw.batchprocessingworker.infrastructure.messaging.rabbitmq.events;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record FileUploaded(UserToken userToken, FileToken fileToken, String fileName, LocalDateTime occurredOn) { }
