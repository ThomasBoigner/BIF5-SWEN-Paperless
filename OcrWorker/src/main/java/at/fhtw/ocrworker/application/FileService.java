package at.fhtw.ocrworker.application;

import org.apache.pdfbox.pdmodel.PDDocument;

import java.util.UUID;

public interface FileService {
    PDDocument downloadFile(UUID token);
}
