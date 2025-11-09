package at.fhtw.ocrworker.infrastructure.messaging.rabbitmq.events;

import java.time.LocalDateTime;

public record FileUploaded(byte[] file, FileToken fileToken, LocalDateTime occurredOn) { }
