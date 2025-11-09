package at.fhtw.paperlessrest.infrastructure.messaging.rabbitmq;

import at.fhtw.paperlessrest.application.FileMetaDataApplicationService;
import at.fhtw.paperlessrest.application.commands.AddFullTextCommand;
import at.fhtw.paperlessrest.infrastructure.messaging.rabbitmq.events.TextExtracted;
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
public class RabbitMqTextExtractedListener {
    private final FileMetaDataApplicationService fileMetaDataApplicationService;
    private final ObjectMapper objectMapper;

    @RabbitListener(queues = "at.fhtw.ocrworker.domain.model.textextracted")
    public void receiveTextExtractedEvent(@Nullable String in) {
        try {
            TextExtracted event = objectMapper.readValue(in, TextExtracted.class);
            log.trace("Received text extracted event: {}", event);
            fileMetaDataApplicationService.addFullText(AddFullTextCommand.builder()
                    .FullText(event.fullText())
                    .fileToken(event.fileToken().token())
                    .build());
        } catch (JsonProcessingException e) {
            log.error("Could not deserialize TextExtracted event", e);
        }
    }
}
