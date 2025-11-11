package at.fhtw.ocrworker.application;

public interface OcrService {
    String extractText(byte[] imageBytes);
}
