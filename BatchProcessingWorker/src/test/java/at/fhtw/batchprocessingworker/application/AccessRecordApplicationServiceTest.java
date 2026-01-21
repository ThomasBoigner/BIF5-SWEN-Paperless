package at.fhtw.batchprocessingworker.application;

import at.fhtw.batchprocessingworker.application.commands.CreateAccessRecordCommand;
import at.fhtw.batchprocessingworker.domain.model.AccessRecord;
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
public class AccessRecordApplicationServiceTest {
    private AccessRecordApplicationService accessRecordApplicationService;
    @Mock
    private XmlService xmlService;

    @BeforeEach
    void setUp() {
        accessRecordApplicationService = new AccessRecordApplicationService(xmlService);
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
}
