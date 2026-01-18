package at.fhtw.paperlessrest.infrastructure.messaging.rabbitmq;

import at.fhtw.paperlessrest.application.FileMetaDataApplicationService;
import at.fhtw.paperlessrest.application.commands.AddSummaryCommand;
import at.fhtw.paperlessrest.domain.model.UserToken;
import at.fhtw.paperlessrest.infrastructure.messaging.rabbitmq.events.FileToken;
import at.fhtw.paperlessrest.infrastructure.messaging.rabbitmq.events.SummaryCreated;
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
public class RabbitMQGenAiWorkerListenerTest {
    private RabbitMQGenAiWorkerListener listener;
    @Mock
    private FileMetaDataApplicationService fileMetaDataApplicationService;

    @BeforeEach
    void setUp() {
        listener = new RabbitMQGenAiWorkerListener(fileMetaDataApplicationService);
    }

    @Test
    void ensureReceiveSummaryCreatedEventWorksProperly() {
        // Given
        String summary = "Summary";
        UUID userToken = UUID.randomUUID();
        UUID fileToken = UUID.randomUUID();

        SummaryCreated event = SummaryCreated.builder()
                .userToken(new UserToken(userToken))
                .fileToken(new FileToken(fileToken))
                .summary(summary)
                .build();

        // When
        listener.receiveSummaryCreatedEvent(event);

        // Then
        verify(fileMetaDataApplicationService).addSummary(eq(AddSummaryCommand.builder()
                .fileToken(fileToken)
                .summary(summary)
                .userToken(userToken)
                .build())
        );
    }
}
