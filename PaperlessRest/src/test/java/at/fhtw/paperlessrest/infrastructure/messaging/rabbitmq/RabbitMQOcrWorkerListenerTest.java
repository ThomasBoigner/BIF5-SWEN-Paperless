package at.fhtw.paperlessrest.infrastructure.messaging.rabbitmq;

import at.fhtw.paperlessrest.application.FileMetaDataApplicationService;
import at.fhtw.paperlessrest.application.commands.AddFullTextCommand;
import at.fhtw.paperlessrest.infrastructure.messaging.rabbitmq.events.FileToken;
import at.fhtw.paperlessrest.infrastructure.messaging.rabbitmq.events.TextExtracted;
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

    @BeforeEach
    void setUp() {
        listener = new RabbitMQOcrWorkerListener(fileMetaDataApplicationService);
    }

    @Test
    void ensureReceiveTextExtractedEventWorksProperly() {
        // Given
        String fullText = "Full Text";

        UUID fileToken = UUID.randomUUID();

        TextExtracted event = TextExtracted.builder()
                .fileToken(new FileToken(fileToken))
                .fullText(fullText)
                .build();

        // When
        listener.receiveTextExtractedEvent(event);

        // Then
        verify(fileMetaDataApplicationService).addFullText(eq(AddFullTextCommand.builder()
                .fileToken(fileToken)
                .FullText(fullText)
                .build())
        );
    }
}
