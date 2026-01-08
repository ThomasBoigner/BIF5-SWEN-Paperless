package at.fhtw.genaiworker.infrastructure.messaging.rabbitmq;

import at.fhtw.genaiworker.application.SummaryCreatedEventPublisher;
import at.fhtw.genaiworker.domain.model.SummaryCreated;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor

@Slf4j
@Component
public class RabbitMQSummaryCreatedEventPublisher implements SummaryCreatedEventPublisher {
    private final RabbitTemplate template;
    private final TopicExchange genAiWorkerTopic;

    @Override
    public void publishEvent(SummaryCreated event) {
        log.trace("Publishing summary created event: {}", event);
        template.convertAndSend(genAiWorkerTopic.getName(), "at.fhtw.genaiworker.domain.model.summarycreated", event);
    }
}
