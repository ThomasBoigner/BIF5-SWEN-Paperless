package at.fhtw.ocrworker.application;

import at.fhtw.ocrworker.application.commands.ExtractTextCommand;
import at.fhtw.ocrworker.domain.model.TextExtracted;
import net.sourceforge.tess4j.TesseractException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.jspecify.annotations.NullUnmarked;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@NullUnmarked
@ExtendWith(MockitoExtension.class)
public class OcrApplicationServiceTest {
    private TextExtractionApplicationService textExtractionApplicationService;
    @Mock
    private OcrService ocrService;
    @Mock
    private FileService fileService;
    @Mock
    private TextExtractedEventPublisher textExtractedEventPublisher;

    @BeforeEach
    void setUp() {
        textExtractionApplicationService = new TextExtractionApplicationService(ocrService, fileService, textExtractedEventPublisher);
    }

    @Test
    void ensureExtractTextWorksProperly() throws TesseractException {
        // Given
        String fullText = "Full Text";

        UUID fileToken = UUID.randomUUID();

        PDDocument pdf = mock(PDDocument.class);

        ExtractTextCommand command = ExtractTextCommand.builder()
                .fileToken(fileToken)
                .build();

        when(fileService.downloadFile(eq(fileToken))).thenReturn(pdf);
        when(ocrService.extractText(eq(pdf))).thenReturn(fullText);

        // When
        textExtractionApplicationService.extractText(command);

        // Then
        verify(textExtractedEventPublisher).publishEvent(any(TextExtracted.class));
    }
}
