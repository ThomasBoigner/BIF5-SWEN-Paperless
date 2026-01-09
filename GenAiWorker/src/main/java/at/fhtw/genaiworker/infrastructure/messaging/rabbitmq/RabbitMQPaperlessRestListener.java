package at.fhtw.genaiworker.infrastructure.messaging.rabbitmq;

import at.fhtw.genaiworker.application.SummaryApplicationService;
import at.fhtw.genaiworker.application.commands.CreateSummaryCommand;
import at.fhtw.genaiworker.infrastructure.messaging.rabbitmq.events.FullTextAdded;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.Nullable;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Objects;

@RequiredArgsConstructor

@Slf4j
@Component
public class RabbitMQPaperlessRestListener {
    private final SummaryApplicationService summaryService;

    @RabbitListener(queues = "at.fhtw.paperlessrest.domain.model.fulltextadded")
    public void receiveFullTextAddedEvent(@Nullable FullTextAdded event) {
        if (Objects.isNull(event)) {
            log.warn("Received event was null!");
            return;
        }
        log.trace("Received full text added event: {}", event);
        summaryService.createSummary(CreateSummaryCommand.builder()
                        .userToken(event.userToken().token())
                        .fileToken(event.fileToken().token())
                        .fullText(event.fullText())
                .build());
    }
}