package at.fhtw.ocrworker.infrastructure.messaging.rabbitmq;

import at.fhtw.ocrworker.application.TextExtractionApplicationService;
import at.fhtw.ocrworker.application.commands.ExtractTextCommand;
import at.fhtw.ocrworker.infrastructure.messaging.rabbitmq.events.FileUploaded;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.Nullable;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Objects;

@RequiredArgsConstructor

@Slf4j
@Component
public class RabbitMQFilePaperlessRestListener {
    private final TextExtractionApplicationService textExtractionApplicationService;

    @RabbitListener(queues = "at.fhtw.paperlessrest.domain.model.fileuploaded")
    public void receiveFileUploadedEvent(@Nullable FileUploaded event) {
        if (Objects.isNull(event)) {
            log.warn("Received event was null!");
            return;
        }
        log.trace("Received file uploaded event: {}", event);
        textExtractionApplicationService.extractText(ExtractTextCommand.builder()
                        .fileToken(event.fileToken().token())
                        .userToken(event.userToken().token())
                        .fileName(event.fileName()
                        )
                .build());
    }
}
