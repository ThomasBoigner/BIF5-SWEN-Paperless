package at.fhtw.ocrworker.application;

import at.fhtw.ocrworker.application.commands.ExtractTextCommand;
import at.fhtw.ocrworker.domain.model.FileToken;
import at.fhtw.ocrworker.domain.model.TextExtracted;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Objects;

@RequiredArgsConstructor

@Service
@Slf4j
public class TextExtractionApplicationService {
    private final OcrService ocrService;
    private final TextExtractedEventPublisher textExtractedEventPublisher;

    public void extractText(@Nullable ExtractTextCommand command) {
        Objects.requireNonNull(command, "command must not be null!");
        log.debug("Trying to extract full text with command: {}", command);

        String fullText = ocrService.extractText(command.imageBytes());
        log.info("Extracted full text {} from image", fullText);

        TextExtracted textExtracted = TextExtracted.builder()
                .fullText(fullText)
                .fileToken(new FileToken(command.fileToken()))
                .build();

        textExtractedEventPublisher.publishEvent(textExtracted);
    }
}
