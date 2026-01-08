package at.fhtw.genaiworker.infrastructure.messaging.rabbitmq.events;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record FullTextAdded(String fullText, UserToken userToken, FileToken fileToken, LocalDateTime createdAt) { }
