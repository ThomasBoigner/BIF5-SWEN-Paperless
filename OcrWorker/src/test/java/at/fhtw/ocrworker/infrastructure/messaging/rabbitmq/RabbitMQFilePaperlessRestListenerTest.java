package at.fhtw.ocrworker.infrastructure.messaging.rabbitmq;

import at.fhtw.ocrworker.application.TextExtractionApplicationService;
import at.fhtw.ocrworker.application.commands.ExtractTextCommand;
import at.fhtw.ocrworker.infrastructure.messaging.rabbitmq.events.FileToken;
import at.fhtw.ocrworker.infrastructure.messaging.rabbitmq.events.FileUploaded;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.jspecify.annotations.NullUnmarked;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@NullUnmarked
@ExtendWith(MockitoExtension.class)
public class RabbitMQFilePaperlessRestListenerTest {
    private RabbitMQFilePaperlessRestListener listener;
    @Mock
    private TextExtractionApplicationService textExtractionApplicationService;

    @BeforeEach
    void setUp() {
        listener = new RabbitMQFilePaperlessRestListener(textExtractionApplicationService);
    }

    @Test
    void ensureReceiveFileUploadedEvent() throws JsonProcessingException {
        // Given
        UUID fileToken = UUID.randomUUID();

        FileUploaded event = FileUploaded.builder()
                .fileToken(new FileToken(fileToken))
                .build();

        // When
        listener.receiveFileUploadedEvent(event);

        // Then
        verify(textExtractionApplicationService).extractText(any(ExtractTextCommand.class));
    }
}
