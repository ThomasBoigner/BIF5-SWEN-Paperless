package at.fhtw.genaiworker.infrastructure.messaging.rabbitmq;

import at.fhtw.genaiworker.domain.model.SummaryService;
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
    private final SummaryService summaryService;

    @RabbitListener(queues = "at.fhtw.paperlessrest.domain.model.fulltextadded")
    public void handleOcrFinished(@Nullable FullTextAdded event) {
        if (Objects.isNull(event)) {
            log.warn("Received event was null!");
            return;
        }
        log.trace("Received full text added event: {}", event);
        String summary = summaryService.createSummary(event.fullText());
        log.info("created summary: {}", summary);
    }
}