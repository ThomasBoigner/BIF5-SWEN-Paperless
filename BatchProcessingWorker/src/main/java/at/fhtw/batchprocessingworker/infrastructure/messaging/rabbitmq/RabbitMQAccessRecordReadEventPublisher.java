package at.fhtw.batchprocessingworker.infrastructure.messaging.rabbitmq;

import at.fhtw.batchprocessingworker.application.AccessRecordReadEventPublisher;
import at.fhtw.batchprocessingworker.domain.model.AccessRecordRead;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor

@Slf4j
@Component
public class RabbitMQAccessRecordReadEventPublisher implements AccessRecordReadEventPublisher {
    private final RabbitTemplate template;
    private final TopicExchange batchProcessingWorkerTopic;

    @Override
    public void publishEvents(AccessRecordRead event) {
        log.trace("Publishing access record read event: {}", event);
        template.convertAndSend(batchProcessingWorkerTopic.getName(), "at.fhtw.batchprocessingworker.domain.model.accessrecordread", event);
    }
}
