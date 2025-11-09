package at.fhtw.ocrworker.infrastructure.configuration;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfiguration {
    @Bean
    public TopicExchange topic() {
        return new TopicExchange("at.fhtw.paperlessrest", true, false);
    }

    @Bean
    public Queue fileUploadedQueue() {
        return QueueBuilder.durable("at.fhtw.paperlessrest.domain.model.fileuploaded").build();
    }

    @Bean
    public Binding fileUploadedBinding() {
        return BindingBuilder.bind(fileUploadedQueue()).to(topic()).with("at.fhtw.paperlessrest.domain.model.fileuploaded");
    }
}
