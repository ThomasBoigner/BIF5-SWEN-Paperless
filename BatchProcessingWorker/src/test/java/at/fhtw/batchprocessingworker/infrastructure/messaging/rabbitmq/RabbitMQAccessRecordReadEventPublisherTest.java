package at.fhtw.batchprocessingworker.infrastructure.messaging.rabbitmq;

import at.fhtw.batchprocessingworker.domain.model.AccessRecordRead;
import at.fhtw.batchprocessingworker.domain.model.FileToken;
import at.fhtw.batchprocessingworker.domain.model.UserToken;
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
public class RabbitMQAccessRecordReadEventPublisherTest {
    private RabbitMQAccessRecordReadEventPublisher publisher;
    @Mock
    private RabbitTemplate rabbitTemplate;

    @BeforeEach
    void setUp() {
        TopicExchange topicExchange = new TopicExchange("at.fhtw.batchprocessingworker", true, false);
        publisher = new RabbitMQAccessRecordReadEventPublisher(rabbitTemplate, topicExchange);
    }

    @Test
    void ensurePublishAccessRecordReadEventWorksProperly() {
        // Given
        AccessRecordRead event = AccessRecordRead.builder()
                .fileToken(new FileToken(UUID.randomUUID()))
                .userToken(new UserToken(UUID.randomUUID()))
                .numberOfAccesses(2)
                .build();

        // When
        publisher.publishEvents(event);

        // Then
        verify(rabbitTemplate).convertAndSend(
                eq("at.fhtw.batchprocessingworker"),
                eq("at.fhtw.batchprocessingworker.domain.model.accessrecordread"),
                eq(event)
        );
    }
}
