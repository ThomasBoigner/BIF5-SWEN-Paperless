package at.fhtw.paperlessrest.infrastructure.messaging.rabbitmq;

import at.fhtw.paperlessrest.application.FileMetaDataApplicationService;
import at.fhtw.paperlessrest.application.commands.AddNumberOfAccessesCommand;
import at.fhtw.paperlessrest.infrastructure.messaging.rabbitmq.events.AccessRecordRead;
import at.fhtw.paperlessrest.infrastructure.messaging.rabbitmq.events.FileToken;
import at.fhtw.paperlessrest.infrastructure.messaging.rabbitmq.events.UserToken;
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
public class RabbitMQBatchProcessingWorkerListenerTest {
    private RabbitMQBatchProcessingWorkerListener listener;
    @Mock
    private FileMetaDataApplicationService fileMetaDataApplicationService;

    @BeforeEach
    void setUp() {
        listener = new RabbitMQBatchProcessingWorkerListener(fileMetaDataApplicationService);
    }

    @Test
    void ensureReceiveAccessRecordReadEventWorksProperly() {
        // Given
        int numberOfAccesses = 5;
        UUID userToken = UUID.randomUUID();
        UUID fileToken = UUID.randomUUID();

        AccessRecordRead event = AccessRecordRead.builder()
                .userToken(new UserToken(userToken))
                .fileToken(new FileToken(fileToken))
                .numberOfAccesses(numberOfAccesses)
                .build();

        // When
        listener.receiveAccessRecordReadEvent(event);

        // Then
        verify(fileMetaDataApplicationService).addNumberOfAccesses(eq(AddNumberOfAccessesCommand.builder()
                .userToken(userToken)
                .fileToken(fileToken)
                .numberOfAccesses(numberOfAccesses)
                .build()));
    }
}
