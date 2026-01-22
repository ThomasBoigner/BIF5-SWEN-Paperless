package at.fhtw.batchprocessingworker.infrastructure.messaging.rabbitmq;

import at.fhtw.batchprocessingworker.application.AccessRecordApplicationService;
import at.fhtw.batchprocessingworker.application.commands.CreateAccessRecordCommand;
import at.fhtw.batchprocessingworker.infrastructure.messaging.rabbitmq.events.FileUploaded;
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
    private final AccessRecordApplicationService accessRecordApplicationService;

    @RabbitListener(queues = "at.fhtw.paperlessrest.domain.model.fileuploaded.batchprocessingworker")
    public void receiveFileUploadedEvent(@Nullable FileUploaded event) {
        if (Objects.isNull(event)) {
            log.warn("Received event was null!");
            return;
        }
        log.trace("Received file uploaded event: {}", event);
        accessRecordApplicationService.createAccessRecord(CreateAccessRecordCommand.builder()
                        .fileToken(event.fileToken().token())
                        .userToken(event.userToken().token())
                .build());
    }
}
