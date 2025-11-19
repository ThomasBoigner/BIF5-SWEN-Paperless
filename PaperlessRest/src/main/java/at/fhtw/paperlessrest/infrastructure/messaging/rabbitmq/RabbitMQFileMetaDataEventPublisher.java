package at.fhtw.paperlessrest.infrastructure.messaging.rabbitmq;

import at.fhtw.paperlessrest.application.FileMetaDataEventPublisher;
import at.fhtw.paperlessrest.domain.model.FileMetaData;
import at.fhtw.paperlessrest.domain.model.FileUploaded;
import at.fhtw.paperlessrest.domain.model.FullTextAdded;
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

    @Override
    public void publishEvents(FileMetaData fileMetaData) {
        publishFileUploadedEvents(fileMetaData.getFileUploadedEvents());
        publishFullTextAddedEvents(fileMetaData.getFullTextAddedEvents());
    }

    private void publishFileUploadedEvents(List<FileUploaded> events) {
        events.forEach(event -> {
            log.trace("Publishing file uploaded event: {}", event);
            template.convertAndSend(paperlessRestTopic.getName(), "at.fhtw.paperlessrest.domain.model.fileuploaded", event);
        });
    }

    private void publishFullTextAddedEvents(List<FullTextAdded> events) {
        events.forEach(event -> {
            log.trace("Publishing full text added event: {}", event);
            template.convertAndSend(paperlessRestTopic.getName(), "at.fhtw.paperlessrest.domain.model.fulltextadded", event);
        });
    }
}
