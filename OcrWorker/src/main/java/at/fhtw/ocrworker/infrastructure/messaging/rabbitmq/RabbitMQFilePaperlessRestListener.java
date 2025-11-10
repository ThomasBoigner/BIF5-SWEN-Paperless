package at.fhtw.ocrworker.infrastructure.messaging.rabbitmq;

import at.fhtw.ocrworker.application.OcrApplicationService;
import at.fhtw.ocrworker.application.commands.ExtractTextCommand;
import at.fhtw.ocrworker.infrastructure.messaging.rabbitmq.events.FileUploaded;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.Nullable;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor

@Slf4j
@Component
public class RabbitMQFilePaperlessRestListener {
    private final OcrApplicationService ocrApplicationService;
    private final ObjectMapper objectMapper;

    @RabbitListener(queues = "at.fhtw.paperlessrest.domain.model.fileuploaded")
    public void receiveFileUploadedEvent(@Nullable String in) {
        try {
            FileUploaded event = objectMapper.readValue(in, FileUploaded.class);
            log.trace("Received file uploaded event: {}", event);
            ocrApplicationService.extractText(ExtractTextCommand.builder()
                            .imageBytes(event.file())
                            .fileToken(event.fileToken().token())
                    .build());
        } catch (JsonProcessingException e) {
            log.error("Could not deserialize FileUploaded event", e);
        }
    }
}
