package at.fhtw.ocrworker.application;

import at.fhtw.ocrworker.domain.model.TextExtracted;

public interface TextExtractedEventPublisher {
    void publishEvent(TextExtracted event);
}
