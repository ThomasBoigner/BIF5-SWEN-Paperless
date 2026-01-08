package at.fhtw.genaiworker.application;

import at.fhtw.genaiworker.application.commands.CreateSummaryCommand;
import at.fhtw.genaiworker.domain.model.FileToken;
import at.fhtw.genaiworker.domain.model.SummaryCreated;
import at.fhtw.genaiworker.domain.model.UserToken;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.Nullable;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;

@RequiredArgsConstructor

@Slf4j
@Service
public class SummaryApplicationService {
    private final ChatClient chatClient;
    private final SummaryCreatedEventPublisher summaryCreatedEventPublisher;

    private final static PromptTemplate PROMPT_TEMPLATE = new PromptTemplate("Summarize the following document in 150 words or less. {content}");

    public void createSummary(@Nullable CreateSummaryCommand command) {
        Objects.requireNonNull(command, "command must not be null!");
        log.debug("Trying to create summary with command: {}", command);

        Prompt prompt = PROMPT_TEMPLATE.create(Map.of("content", command.fullText()));

        String summary = chatClient
                .prompt(prompt)
                .call()
                .content();

        log.info("Created summary {} from full text", summary);

        SummaryCreated summaryCreated = SummaryCreated.builder()
                .summary(summary != null ? summary : "")
                .userToken(new UserToken(command.userToken()))
                .fileToken(new FileToken(command.fileToken()))
                .build();

        summaryCreatedEventPublisher.publishEvent(summaryCreated);
    }
}
