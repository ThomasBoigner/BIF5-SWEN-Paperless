package at.fhtw.genaiworker.infrastructure;

import at.fhtw.genaiworker.domain.SummaryService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class GenAiWorkerRabbitListener {

    private final SummaryService summaryService;
    private final MinioAdapter minioAdapter;

    public GenAiWorkerRabbitListener(SummaryService summaryService, MinioAdapter minioAdapter) {
        this.summaryService = summaryService;
        this.minioAdapter = minioAdapter;
    }

    @RabbitListener(queues = "ocr_done_queue")
    public void handleOcrFinished(String fileKey) {
        try {
            String ocrText = minioAdapter.fetchOcrText("ocr-bucket", fileKey);
            String summary = summaryService.createSummary(ocrText);
            String summaryKey = fileKey.replace("ocr-texts/", "summaries/");
            minioAdapter.saveSummary("summary-bucket", summaryKey, summary);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}