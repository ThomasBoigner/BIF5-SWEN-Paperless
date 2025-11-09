package at.fhtw.ocrworker.infrastructure.messaging.rabbitmq;

import at.fhtw.ocrworker.application.OcrService;
import at.fhtw.ocrworker.application.commands.ExtractTextCommand;
import at.fhtw.ocrworker.infrastructure.messaging.rabbitmq.events.FileUploaded;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor

@Slf4j
@Component
public class RabbitMQFileUploadedEventListener {
    private final OcrService ocrService;
    private final ObjectMapper objectMapper;

    @RabbitListener(queues = "at.fhtw.paperlessrest.domain.model.fileuploaded")
    public void receiveFileUploadedEvent(String in) {
        try {
            FileUploaded event = objectMapper.readValue(in, FileUploaded.class);
            log.trace("Received file uploaded event: {}", event);
            ocrService.extractText(ExtractTextCommand.builder()
                            .imageBytes(event.file())
                    .build());
        } catch (JsonProcessingException e) {
            log.error("Could not deserialize UserRegistered event", e);
        }
    }
}
