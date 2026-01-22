package at.fhtw.batchprocessingworker.application;

import at.fhtw.batchprocessingworker.application.commands.CreateAccessRecordCommand;
import at.fhtw.batchprocessingworker.domain.model.AccessRecord;
import at.fhtw.batchprocessingworker.domain.model.AccessRecordRead;
import at.fhtw.batchprocessingworker.domain.model.FileToken;
import at.fhtw.batchprocessingworker.domain.model.UserToken;
import org.jspecify.annotations.NullUnmarked;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@NullUnmarked
@ExtendWith(MockitoExtension.class)
public class AccessRecordApplicationServiceTest {
    private AccessRecordApplicationService accessRecordApplicationService;
    @Mock
    private XmlService xmlService;
    @Mock
    private AccessRecordReadEventPublisher accessRecordReadEventPublisher;

    @BeforeEach
    void setUp() {
        accessRecordApplicationService = new AccessRecordApplicationService(xmlService, accessRecordReadEventPublisher);
    }

    @Test
    void ensureCreateAccessRecordWorksProperly() {
        // Given
        CreateAccessRecordCommand command = CreateAccessRecordCommand.builder()
                .userToken(UUID.randomUUID())
                .fileToken(UUID.randomUUID())
                .build();

        // When
        accessRecordApplicationService.createAccessRecord(command);

        // Then
        verify(xmlService).writeAccessRecordToFile(any(AccessRecord.class));
    }

    @Test
    void ensureReadAccessRecordsWorksProperly() {
        // Given
        AccessRecord accessRecord = AccessRecord.builder()
                .userToken(new UserToken(UUID.randomUUID()))
                .fileToken(new FileToken(UUID.randomUUID()))
                .build();

        when(xmlService.readAccessRecords()).thenReturn(List.of(accessRecord));

        // When
        accessRecordApplicationService.readAccessRecords();

        // Then
        verify(accessRecordReadEventPublisher).publishEvents(any(AccessRecordRead.class));
        verify(xmlService).deleteAccessRecord(eq(accessRecord.getAccessRecordToken().token()));
    }
}
