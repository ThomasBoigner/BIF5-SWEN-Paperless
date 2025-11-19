package at.fhtw.ocrworker.application;

import org.apache.pdfbox.pdmodel.PDDocument;

public interface OcrService {
    String extractText(PDDocument imageBytes);
}
