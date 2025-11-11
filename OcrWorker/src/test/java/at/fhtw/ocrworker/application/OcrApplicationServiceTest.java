package at.fhtw.ocrworker.application;

import at.fhtw.ocrworker.application.commands.ExtractTextCommand;
import at.fhtw.ocrworker.domain.model.TextExtracted;
import net.sourceforge.tess4j.TesseractException;
import org.jspecify.annotations.NullUnmarked;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@NullUnmarked
@ExtendWith(SpringExtension.class)
public class OcrApplicationServiceTest {
    private TextExtractionApplicationService textExtractionApplicationService;
    @Mock
    private OcrService ocrService;
    @Mock
    private TextExtractedEventPublisher textExtractedEventPublisher;

    @BeforeEach
    void setUp() {
        textExtractionApplicationService = new TextExtractionApplicationService(ocrService, textExtractedEventPublisher);
    }

    @Test
    void ensureExtractTextWorksProperly() throws TesseractException {
        // Given
        String fullText = "Full Text";

        UUID fileToken = UUID.randomUUID();

        ExtractTextCommand command = ExtractTextCommand.builder()
                .imageBytes(new byte[8])
                .fileToken(fileToken)
                .build();

        when(ocrService.extractText(eq(command.imageBytes()))).thenReturn(fullText);

        // When
        textExtractionApplicationService.extractText(command);

        // Then
        verify(textExtractedEventPublisher).publishEvent(any(TextExtracted.class));
    }
}
