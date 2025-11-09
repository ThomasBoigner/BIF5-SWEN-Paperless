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
public class OcrApplicationService {
    private final Tesseract tesseract;
    private final TextExtractedEventPublisher textExtractedEventPublisher;

    public void extractText(@Nullable ExtractTextCommand command) {
        Objects.requireNonNull(command, "command must not be null!");
        log.debug("Trying to extract full text with command: {}", command);

        StringBuilder fullTextBuilder = new StringBuilder();
        try(PDDocument document = Loader.loadPDF(command.imageBytes())) {
            PDFRenderer renderer = new PDFRenderer(document);
            for (int pageNumber = 0; pageNumber < document.getNumberOfPages(); pageNumber++) {
                fullTextBuilder.append(tesseract.doOCR(renderer.renderImageWithDPI(pageNumber, 70)));
            }
        } catch (IOException e) {
            log.error("Could not create buffered image from byte array {}.", command.imageBytes(), e);
            throw new RuntimeException(e);
        } catch (TesseractException e) {
            log.error("Could not extract full text from byte array {}.", command.imageBytes(), e);
            throw new RuntimeException(e);
        }

        String fullText = fullTextBuilder.toString();
        log.info("Extracted full text {} from image", fullText);

        TextExtracted textExtracted = TextExtracted.builder()
                .fullText(fullText)
                .fileToken(new FileToken(command.fileToken()))
                .build();

        textExtractedEventPublisher.publishEvent(textExtracted);
    }
}
