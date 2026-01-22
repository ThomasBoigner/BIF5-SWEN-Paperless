package at.fhtw.batchprocessingworker.infrastructure.messaging.rabbitmq;

import at.fhtw.batchprocessingworker.application.AccessRecordApplicationService;
import at.fhtw.batchprocessingworker.application.commands.CreateAccessRecordCommand;
import at.fhtw.batchprocessingworker.infrastructure.messaging.rabbitmq.events.FileToken;
import at.fhtw.batchprocessingworker.infrastructure.messaging.rabbitmq.events.FileUploaded;
import at.fhtw.batchprocessingworker.infrastructure.messaging.rabbitmq.events.UserToken;
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
public class RabbitMQPaperlessRestListenerTest {
    private RabbitMQPaperlessRestListener listener;
    @Mock
    private AccessRecordApplicationService accessRecordApplicationService;

    @BeforeEach
    void setUp() {
        listener = new RabbitMQPaperlessRestListener(accessRecordApplicationService);
    }

    @Test
    void ensureReceiveFileUploadEventWorksProperly() {
        // Given
        String fileName = "abc";
        UUID userToken = UUID.randomUUID();
        UUID fileToken = UUID.randomUUID();

        FileUploaded event = FileUploaded.builder()
                .fileName(fileName)
                .userToken(new UserToken(userToken))
                .fileToken(new FileToken(fileToken))
                .build();

        // When
        listener.receiveFileUploadedEvent(event);

        // Then
        verify(accessRecordApplicationService).createAccessRecord(eq(CreateAccessRecordCommand.builder()
                .userToken(userToken)
                .fileToken(fileToken)
                .build()));
    }
}
