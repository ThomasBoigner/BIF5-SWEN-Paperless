package at.fhtw.genaiworker.infrastructure;

import at.fhtw.genaiworker.domain.model.FileToken;
import at.fhtw.genaiworker.domain.model.SummaryCreated;
import at.fhtw.genaiworker.domain.model.UserToken;
import at.fhtw.genaiworker.infrastructure.messaging.rabbitmq.RabbitMQSummaryCreatedEventPublisher;
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
public class RabbitMQSummaryCreatedEventPublisherTest {
    private RabbitMQSummaryCreatedEventPublisher publisher;
    @Mock
    private RabbitTemplate rabbitTemplate;

    @BeforeEach
    void setUp() {
        TopicExchange topicExchange = new TopicExchange("at.fhtw.genaiworker", true, false);
        publisher = new RabbitMQSummaryCreatedEventPublisher(rabbitTemplate, topicExchange);
    }

    @Test
    void ensurePublishEventWorksProperly() {
        // Given
        String summary = "summary";

        UUID fileToken = UUID.randomUUID();
        UUID userToken = UUID.randomUUID();

        SummaryCreated event = SummaryCreated.builder()
                .fileToken(new FileToken(fileToken))
                .userToken(new UserToken(userToken))
                .summary(summary)
                .build();

        // When
        publisher.publishEvent(event);

        // Then
        verify(rabbitTemplate).convertAndSend(
                eq("at.fhtw.genaiworker"),
                eq("at.fhtw.genaiworker.domain.model.summarycreated"),
                eq(event)
        );
    }
}
