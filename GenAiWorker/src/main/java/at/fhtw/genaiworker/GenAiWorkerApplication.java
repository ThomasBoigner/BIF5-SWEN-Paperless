package at.fhtw.genaiworker;

import at.fhtw.genaiworker.application.port.GenAiPort;
import at.fhtw.genaiworker.domain.SummaryService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class GenAiWorkerApplication {
    public static void main(String[] args) {
        SpringApplication.run(GenAiWorkerApplication.class, args);
    }

    @Bean
    public SummaryService summaryService(GenAiPort genAiPort) {
        return new SummaryService(genAiPort);
    }
}
