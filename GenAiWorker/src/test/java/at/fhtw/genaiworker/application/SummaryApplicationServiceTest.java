package at.fhtw.genaiworker.application;

import at.fhtw.genaiworker.application.commands.CreateSummaryCommand;
import at.fhtw.genaiworker.domain.model.SummaryCreated;
import org.jspecify.annotations.NullUnmarked;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@NullUnmarked
@ExtendWith(MockitoExtension.class)
public class SummaryApplicationServiceTest {
    private SummaryApplicationService summaryApplicationService;
    @Mock
    private GenAiService genAiService;
    @Mock
    private SummaryCreatedEventPublisher summaryCreatedEventPublisher;

    @BeforeEach
    void setUp() {
        summaryApplicationService = new SummaryApplicationService(genAiService, summaryCreatedEventPublisher);
    }

    @Test
    void ensureCreateSummaryWorksProperly() {
        // Given
        String fullText = "Full Text";

        UUID userToken = UUID.randomUUID();
        UUID fileToken = UUID.randomUUID();

        CreateSummaryCommand command = CreateSummaryCommand.builder()
                .fullText(fullText)
                .fileToken(fileToken)
                .userToken(userToken)
                .build();

        String summary = "summary";

        when(genAiService.summarize(eq(fullText))).thenReturn(summary);

        // When
        summaryApplicationService.createSummary(command);

        // Then
        verify(summaryCreatedEventPublisher).publishEvent(any(SummaryCreated.class));
    }
}
