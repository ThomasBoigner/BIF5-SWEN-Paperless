package at.fhtw.paperlessrest.infrastructure.messaging.rabbitmq;

import at.fhtw.paperlessrest.application.FileMetaDataApplicationService;
import at.fhtw.paperlessrest.application.commands.AddFullTextCommand;
import at.fhtw.paperlessrest.infrastructure.messaging.rabbitmq.events.TextExtracted;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.Nullable;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Objects;

@RequiredArgsConstructor

@Slf4j
@Component
public class RabbitMQOcrWorkerListener {
    private final FileMetaDataApplicationService fileMetaDataApplicationService;

    @RabbitListener(queues = "at.fhtw.ocrworker.domain.model.textextracted")
    public void receiveTextExtractedEvent(@Nullable TextExtracted event) {
        if (Objects.isNull(event)) {
            log.warn("Received event was null!");
            return;
        }
        log.trace("Received text extracted event: {}", event);
        fileMetaDataApplicationService.addFullText(AddFullTextCommand.builder()
                .FullText(event.fullText())
                .fileToken(event.fileToken().token())
                .build());
    }
}
