package at.fhtw.ocrworker.infrastructure.ocr.tesseract;

import at.fhtw.ocrworker.application.OcrService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.springframework.stereotype.Service;

import java.io.IOException;

@RequiredArgsConstructor

@Slf4j
@Service
public class TesseractOcrService implements OcrService {
    private final Tesseract tesseract;

    @Override
    public String extractText(byte[] imageBytes) {
        log.trace("Trying to extract full text with image");
        StringBuilder fullTextBuilder = new StringBuilder();
        try(PDDocument document = Loader.loadPDF(imageBytes)) {
            PDFRenderer renderer = new PDFRenderer(document);
            for (int pageNumber = 0; pageNumber < document.getNumberOfPages(); pageNumber++) {
                fullTextBuilder.append(tesseract.doOCR(renderer.renderImageWithDPI(pageNumber, 70)));
            }
        } catch (IOException e) {
            log.error("Could not create buffered image from byte array {}.", imageBytes, e);
            throw new RuntimeException(e);
        } catch (TesseractException e) {
            log.error("Could not extract full text from byte array {}.", imageBytes, e);
            throw new RuntimeException(e);
        }
        return fullTextBuilder.toString();
    }
}
