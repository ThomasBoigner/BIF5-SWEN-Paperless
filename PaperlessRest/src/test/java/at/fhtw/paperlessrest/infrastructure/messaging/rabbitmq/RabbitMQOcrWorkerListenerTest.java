package at.fhtw.paperlessrest.infrastructure.messaging.rabbitmq;

import at.fhtw.paperlessrest.application.FileMetaDataApplicationService;
import at.fhtw.paperlessrest.application.commands.AddFullTextCommand;
import at.fhtw.paperlessrest.infrastructure.messaging.rabbitmq.events.FileToken;
import at.fhtw.paperlessrest.infrastructure.messaging.rabbitmq.events.TextExtracted;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.jspecify.annotations.NullUnmarked;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

@NullUnmarked
@ExtendWith(MockitoExtension.class)
public class RabbitMQOcrWorkerListenerTest {
    private RabbitMQOcrWorkerListener listener;
    @Mock
    private FileMetaDataApplicationService fileMetaDataApplicationService;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
        listener = new RabbitMQOcrWorkerListener(fileMetaDataApplicationService, objectMapper);
    }

    @Test
    void ensureReceiveTextExtractedEventWorksProperly() throws JsonProcessingException {
        // Given
        String fullText = "Full Text";

        UUID fileToken = UUID.randomUUID();

        TextExtracted event = TextExtracted.builder()
                .fileToken(new FileToken(fileToken))
                .fullText(fullText)
                .build();

        // When
        listener.receiveTextExtractedEvent(objectMapper.writeValueAsString(event));

        // Then
        verify(fileMetaDataApplicationService).addFullText(eq(AddFullTextCommand.builder()
                .fileToken(fileToken)
                .FullText(fullText)
                .build())
        );
    }
}
