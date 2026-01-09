package at.fhtw.genaiworker.application;

import at.fhtw.genaiworker.application.commands.CreateSummaryCommand;
import at.fhtw.genaiworker.domain.model.FileToken;
import at.fhtw.genaiworker.domain.model.SummaryCreated;
import at.fhtw.genaiworker.domain.model.UserToken;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Service;

import java.util.Objects;

@RequiredArgsConstructor

@Slf4j
@Service
public class SummaryApplicationService {
    private final GenAiService genAiService;
    private final SummaryCreatedEventPublisher summaryCreatedEventPublisher;

    public void createSummary(@Nullable CreateSummaryCommand command) {
        Objects.requireNonNull(command, "command must not be null!");
        log.debug("Trying to create summary with command: {}", command);

        String summary = genAiService.summarize(command.fullText());

        log.info("Created summary {} from full text", summary);

        SummaryCreated summaryCreated = SummaryCreated.builder()
                .summary(summary)
                .userToken(new UserToken(command.userToken()))
                .fileToken(new FileToken(command.fileToken()))
                .build();

        summaryCreatedEventPublisher.publishEvent(summaryCreated);
    }
}
