package at.fhtw.ocrworker.infrastructure.messaging.rabbitmq;

import at.fhtw.ocrworker.application.TextExtractedEventPublisher;
import at.fhtw.ocrworker.domain.model.TextExtracted;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor

@Slf4j
@Component
public class RabbitMQTextExtractedEventPublisher implements TextExtractedEventPublisher {
    private final RabbitTemplate template;
    private final TopicExchange ocrWorkerTopic;

    @Override
    public void publishEvent(TextExtracted event) {
        log.trace("Publishing text extracted event: {}", event);
        template.convertAndSend(ocrWorkerTopic.getName(), "at.fhtw.ocrworker.domain.model.textextracted", event);
    }
}
