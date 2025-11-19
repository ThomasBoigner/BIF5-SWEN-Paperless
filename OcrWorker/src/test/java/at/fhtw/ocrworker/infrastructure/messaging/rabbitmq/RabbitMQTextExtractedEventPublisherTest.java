package at.fhtw.ocrworker.infrastructure.messaging.rabbitmq;

import at.fhtw.ocrworker.domain.model.FileToken;
import at.fhtw.ocrworker.domain.model.TextExtracted;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.jspecify.annotations.NullUnmarked;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

@NullUnmarked
@ExtendWith(MockitoExtension.class)
public class RabbitMQTextExtractedEventPublisherTest {
    private RabbitMQTextExtractedEventPublisher publisher;
    @Mock
    private RabbitTemplate rabbitTemplate;

    @BeforeEach
    void setUp() {
        TopicExchange topicExchange = new TopicExchange("at.fhtw.ocrworker", true, false);
        publisher = new RabbitMQTextExtractedEventPublisher(rabbitTemplate, topicExchange);
    }

    @Test
    void ensurePublishEventWorksProperly() throws JsonProcessingException {
        // Given
        String fullText = "Full Text";

        UUID fileToken = UUID.randomUUID();

        TextExtracted event = TextExtracted.builder()
                .fullText(fullText)
                .fileToken(new FileToken(fileToken))
                .build();

        // When
        publisher.publishEvent(event);

        // Then
        verify(rabbitTemplate).convertAndSend(
                eq("at.fhtw.ocrworker"),
                eq("at.fhtw.ocrworker.domain.model.textextracted"),
                eq(event)
        );
    }
}
