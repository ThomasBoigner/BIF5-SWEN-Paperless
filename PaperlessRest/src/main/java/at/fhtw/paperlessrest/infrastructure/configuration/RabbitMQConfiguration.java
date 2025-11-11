package at.fhtw.paperlessrest.infrastructure.configuration;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfiguration {
    @Bean
    public TopicExchange paperlessRestTopic() {
        return new TopicExchange("at.fhtw.paperlessrest", true, false);
    }

    @Bean
    public TopicExchange ocrWorkerTopic() {
        return new TopicExchange("at.fhtw.ocrworker", true, false);
    }

    @Bean
    public Queue textExtractedQueue() {
        return QueueBuilder.durable("at.fhtw.ocrworker.domain.model.textextracted").build();
    }

    @Bean
    public Binding textExtractedBinding() {
        return BindingBuilder.bind(textExtractedQueue()).to(ocrWorkerTopic()).with("at.fhtw.ocrworker.domain.model.textextracted");
    }
}
