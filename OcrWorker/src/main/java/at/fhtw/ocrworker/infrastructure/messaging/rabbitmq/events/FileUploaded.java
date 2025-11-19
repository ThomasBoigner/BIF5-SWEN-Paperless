package at.fhtw.ocrworker.infrastructure.messaging.rabbitmq.events;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record FileUploaded(FileToken fileToken, LocalDateTime occurredOn) { }
