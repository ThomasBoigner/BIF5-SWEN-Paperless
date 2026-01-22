package at.fhtw.paperlessrest.infrastructure.messaging.rabbitmq;

import at.fhtw.paperlessrest.application.FileMetaDataApplicationService;
import at.fhtw.paperlessrest.application.commands.AddNumberOfAccessesCommand;
import at.fhtw.paperlessrest.infrastructure.messaging.rabbitmq.events.AccessRecordRead;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.Nullable;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Objects;

@RequiredArgsConstructor

@Slf4j
@Component
public class RabbitMQBatchProcessingWorkerListener {
    private final FileMetaDataApplicationService fileMetaDataApplicationService;

    @RabbitListener(queues = "at.fhtw.batchprocessingworker.domain.model.accessrecordread.paperlessrest")
    public void receiveAccessRecordReadEvent(@Nullable AccessRecordRead event) {
        if (Objects.isNull(event)) {
            log.warn("Received event was null!");
            return;
        }
        log.trace("Received access record read event: {}", event);
        fileMetaDataApplicationService.addNumberOfAccesses(AddNumberOfAccessesCommand.builder()
                        .userToken(event.userToken().token())
                        .fileToken(event.fileToken().token())
                        .numberOfAccesses(event.numberOfAccesses())
                .build());
    }
}
