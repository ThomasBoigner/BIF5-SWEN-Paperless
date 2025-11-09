package at.fhtw.ocrworker.infrastructure.configuration;

import net.sourceforge.tess4j.Tesseract;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TesseractConfiguration {
    @Bean
    public Tesseract tesseract() {
        Tesseract tesseract = new Tesseract();
        tesseract.setDatapath("/mnt/data/Bachelor/Semester-5/BIF5-SWEN/BIF5-SWEN-Paperless/OcrWorker/src/main/resources/tessdata");
        tesseract.setLanguage("eng");
        return tesseract;
    }
}
