package at.fhtw.genaiworker.domain;

import at.fhtw.genaiworker.application.port.GenAiPort;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SummaryService {

    private final GenAiPort aiSummaryPort;

    public String createSummary(String ocrText) {
        return aiSummaryPort.summarize(ocrText);
    }
}
