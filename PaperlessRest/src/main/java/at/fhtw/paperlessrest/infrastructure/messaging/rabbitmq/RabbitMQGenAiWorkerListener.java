package at.fhtw.paperlessrest.infrastructure.messaging.rabbitmq;

import at.fhtw.paperlessrest.application.FileMetaDataApplicationService;
import at.fhtw.paperlessrest.application.commands.AddSummaryCommand;
import at.fhtw.paperlessrest.infrastructure.messaging.rabbitmq.events.SummaryCreated;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.Nullable;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Objects;

@RequiredArgsConstructor

@Slf4j
@Component
public class RabbitMQGenAiWorkerListener {
    private final FileMetaDataApplicationService fileMetaDataApplicationService;

    @RabbitListener(queues = "at.fhtw.genaiworker.domain.model.summarycreated.paperlessrest")
    public void receiveSummaryCreatedEvent(@Nullable SummaryCreated event) {
        if (Objects.isNull(event)) {
            log.warn("Received event was null!");
            return;
        }
        log.trace("Received summary created event: {}", event);
        fileMetaDataApplicationService.addSummary(AddSummaryCommand.builder()
                        .summary(event.summary())
                        .userToken(event.userToken().token())
                        .fileToken(event.fileToken().token())
                .build());
    }
}
