package at.fhtw.ocrworker.infrastructure.messaging.rabbitmq;

import at.fhtw.ocrworker.application.TextExtractedEventPublisher;
import at.fhtw.ocrworker.domain.model.TextExtracted;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor

@Slf4j
@Component
public class RabbitMQTextExtractedEventPublisher implements TextExtractedEventPublisher {
    @Override
    public void publishEvent(TextExtracted event) {

    }
}
