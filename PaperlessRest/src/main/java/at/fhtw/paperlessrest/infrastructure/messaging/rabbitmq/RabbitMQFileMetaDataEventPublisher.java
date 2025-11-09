package at.fhtw.paperlessrest.infrastructure.messaging.rabbitmq;

import at.fhtw.paperlessrest.application.FileMetaDataEventPublisher;
import at.fhtw.paperlessrest.domain.model.FileMetaData;
import at.fhtw.paperlessrest.domain.model.FileUploaded;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor

@Slf4j
@Component
public class RabbitMQFileMetaDataEventPublisher implements FileMetaDataEventPublisher {
    private final RabbitTemplate template;
    private final TopicExchange paperlessRestTopic;
    private final ObjectMapper objectMapper;

    @Override
    public void publishEvents(FileMetaData fileMetaData) {
        publishFileUploadedEvents(fileMetaData.getFileUploadedEvents());
    }

    private void publishFileUploadedEvents(List<FileUploaded> events) {
        events.forEach(event -> {
            log.trace("Publishing file uploaded event: {}", event);
            try {
                template.convertAndSend(paperlessRestTopic.getName(), "at.fhtw.paperlessrest.domain.model.fileuploaded", objectMapper.writeValueAsString(event));
            } catch (JsonProcessingException e) {
                log.error("Could not convert FileUploaded {} to JSON.", event, e);
            }
        });
    }
}
