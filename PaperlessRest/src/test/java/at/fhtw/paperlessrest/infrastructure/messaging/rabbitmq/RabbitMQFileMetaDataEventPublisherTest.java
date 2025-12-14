package at.fhtw.paperlessrest.infrastructure.messaging.rabbitmq;

import at.fhtw.paperlessrest.domain.model.FileMetaData;
import at.fhtw.paperlessrest.domain.model.User;
import at.fhtw.paperlessrest.domain.model.UserToken;
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

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;

@NullUnmarked
@ExtendWith(MockitoExtension.class)
public class RabbitMQFileMetaDataEventPublisherTest {
    private RabbitMQUserEventPublisher publisher;
    @Mock
    private RabbitTemplate rabbitTemplate;

    @BeforeEach
    void setUp() {
        TopicExchange topicExchange = new TopicExchange("at.fhtw.paperlessrest", true, false);
        publisher = new RabbitMQUserEventPublisher(rabbitTemplate, topicExchange);
    }

    @Test
    void ensurePublishFileUploadedEventsWorksProperly() throws JsonProcessingException {
        // Given
        User user = User.builder()
                .username("test")
                .userToken(new UserToken(UUID.randomUUID()))
                .build();

        FileMetaData fileMetaData = user.uploadFile("test.txt", 100, "test");

        // When
        publisher.publishEvents(user);

        // Then
        verify(rabbitTemplate).convertAndSend(
                eq("at.fhtw.paperlessrest"),
                eq("at.fhtw.paperlessrest.domain.model.fileuploaded"),
                eq(user.getFileUploadedEvents().getFirst())
        );
    }

    @Test
    void ensurePublishFullTextAddedEventsWorksProperly() throws JsonProcessingException {
        // Given
        User user = User.builder()
                .username("test")
                .userToken(new UserToken(UUID.randomUUID()))
                .build();

        FileMetaData fileMetaData = user.uploadFile("test.txt", 100, "test");

        user.addFullTextToFile(fileMetaData.getFileToken(), "full text");

        // When
        publisher.publishEvents(user);

        // Then
        verify(rabbitTemplate).convertAndSend(
                eq("at.fhtw.paperlessrest"),
                eq("at.fhtw.paperlessrest.domain.model.fulltextadded"),
                eq(fileMetaData.getFullTextAddedEvents().getFirst())
        );
    }
}
