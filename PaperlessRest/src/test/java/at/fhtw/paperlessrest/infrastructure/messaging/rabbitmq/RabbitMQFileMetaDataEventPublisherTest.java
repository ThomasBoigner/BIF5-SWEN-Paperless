package at.fhtw.paperlessrest.infrastructure.messaging.rabbitmq;

import at.fhtw.paperlessrest.domain.model.FileMetaData;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.jspecify.annotations.NullUnmarked;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;

@NullUnmarked
@ExtendWith(MockitoExtension.class)
public class RabbitMQFileMetaDataEventPublisherTest {
    private RabbitMQFileMetaDataEventPublisher publisher;
    @Mock
    private RabbitTemplate rabbitTemplate;
    private TopicExchange topicExchange;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        topicExchange = new TopicExchange("at.fhtw.paperlessrest", true, false);
        objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
        publisher = new RabbitMQFileMetaDataEventPublisher(rabbitTemplate, topicExchange, objectMapper);
    }

    @Test
    void ensurePublishFileUploadedEventsWorksProperly() throws JsonProcessingException {
        // Given
        FileMetaData fileMetaData = FileMetaData.builder()
                .fileName("test.txt")
                .file(new byte[8])
                .description("test")
                .build();

        // When
        publisher.publishEvents(fileMetaData);

        // Then
        verify(rabbitTemplate).convertAndSend(
                eq("at.fhtw.paperlessrest"),
                eq("at.fhtw.paperlessrest.domain.model.fileuploaded"),
                eq(objectMapper.writeValueAsString(fileMetaData.getFileUploadedEvents().getFirst()))
        );
    }

    @Test
    void ensurePublishFullTextAddedEventsWorksProperly() throws JsonProcessingException {
        // Given
        FileMetaData fileMetaData = FileMetaData.builder()
                .fileName("test.txt")
                .file(new byte[8])
                .description("test")
                .build();
        fileMetaData.addFullText("Full Text");

        // When
        publisher.publishEvents(fileMetaData);

        // Then
        verify(rabbitTemplate).convertAndSend(
                eq("at.fhtw.paperlessrest"),
                eq("at.fhtw.paperlessrest.domain.model.fulltextadded"),
                eq(objectMapper.writeValueAsString(fileMetaData.getFullTextAddedEvents().getFirst()))
        );
    }
}
