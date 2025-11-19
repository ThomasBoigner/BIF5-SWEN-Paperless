package at.fhtw.ocrworker.application;

import at.fhtw.ocrworker.application.commands.ExtractTextCommand;
import at.fhtw.ocrworker.domain.model.FileToken;
import at.fhtw.ocrworker.domain.model.TextExtracted;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Objects;

@RequiredArgsConstructor

@Service
@Slf4j
public class TextExtractionApplicationService {
    private final OcrService ocrService;
    private final FileService fileService;
    private final TextExtractedEventPublisher textExtractedEventPublisher;

    public void extractText(@Nullable ExtractTextCommand command) {
        Objects.requireNonNull(command, "command must not be null!");
        log.debug("Trying to extract full text with command: {}", command);

        String fullText = null;
        try {
            fullText = ocrService.extractText(fileService.downloadFile(command.fileToken()).getContentAsByteArray());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        log.info("Extracted full text {} from image", fullText);

        TextExtracted textExtracted = TextExtracted.builder()
                .fullText(fullText)
                .fileToken(new FileToken(command.fileToken()))
                .build();

        textExtractedEventPublisher.publishEvent(textExtracted);
    }
}
