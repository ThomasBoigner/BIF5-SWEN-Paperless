package at.fhtw.ocrworker.infrastructure.messaging.rabbitmq;

import at.fhtw.ocrworker.application.TextExtractedEventPublisher;
import at.fhtw.ocrworker.domain.model.TextExtracted;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
    private final ObjectMapper objectMapper;

    @Override
    public void publishEvent(TextExtracted event) {
        log.trace("Publishing file uploaded event: {}", event);
        try {
            template.convertAndSend(ocrWorkerTopic.getName(), "at.fhtw.ocrworker.domain.model.textextracted", objectMapper.writeValueAsString(event));
        } catch (JsonProcessingException e) {
            log.error("Could not convert TextExtractedEvent {} to JSON.", event, e);
        }
    }
}
