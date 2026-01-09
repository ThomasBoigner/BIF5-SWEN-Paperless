package at.fhtw.genaiworker.infrastructure;

import at.fhtw.genaiworker.application.SummaryApplicationService;
import at.fhtw.genaiworker.application.commands.CreateSummaryCommand;
import at.fhtw.genaiworker.infrastructure.messaging.rabbitmq.RabbitMQPaperlessRestListener;
import at.fhtw.genaiworker.infrastructure.messaging.rabbitmq.events.FileToken;
import at.fhtw.genaiworker.infrastructure.messaging.rabbitmq.events.FullTextAdded;
import at.fhtw.genaiworker.infrastructure.messaging.rabbitmq.events.UserToken;
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
public class RabbitMQPaperlessRestListenerTest {
    private RabbitMQPaperlessRestListener listener;
    @Mock
    private SummaryApplicationService summaryApplicationService;

    @BeforeEach
    void setUp() {
        listener = new RabbitMQPaperlessRestListener(summaryApplicationService);
    }

    @Test
    void ensureReceiveFullTextAddedEventWorksProeprly() throws JsonProcessingException {
        // Given
        UUID fileToken = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        String fullText = "Full Text";

        FullTextAdded event = FullTextAdded.builder()
                .fileToken(new FileToken(fileToken))
                .userToken(new UserToken(userId))
                .fullText(fullText)
                .build();

        // When
        listener.receiveFullTextAddedEvent(event);

        // Then
        verify(summaryApplicationService).createSummary(any(CreateSummaryCommand.class));
    }
}
