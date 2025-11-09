package at.fhtw.ocrworker.infrastructure.configuration;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfiguration {
    @Bean
    public TopicExchange ocrWorkerTopic() {
        return new TopicExchange("at.fhtw.ocrworker", true, false);
    }

    @Bean
    public TopicExchange paperlessRestTopic() {
        return new TopicExchange("at.fhtw.paperlessrest", true, false);
    }

    @Bean
    public Queue fileUploadedQueue() {
        return QueueBuilder.durable("at.fhtw.paperlessrest.domain.model.fileuploaded").build();
    }

    @Bean
    public Binding fileUploadedBinding() {
        return BindingBuilder.bind(fileUploadedQueue()).to(paperlessRestTopic()).with("at.fhtw.paperlessrest.domain.model.fileuploaded");
    }
}
