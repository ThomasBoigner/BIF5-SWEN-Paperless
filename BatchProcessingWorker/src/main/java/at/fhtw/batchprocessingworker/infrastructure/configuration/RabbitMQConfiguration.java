package at.fhtw.batchprocessingworker.infrastructure.configuration;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfiguration {
    @Bean
    public MessageConverter jsonMessageConverter() {
        return new JacksonJsonMessageConverter();
    }

    @Bean
    public AmqpTemplate amqpTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(jsonMessageConverter());
        return template;
    }

    @Bean
    public TopicExchange batchProcessingWorkerTopic() {
        return new TopicExchange("at.fhtw.batchprocessingworker");
    }

    @Bean
    public TopicExchange paperlessRestTopic() {
        return new TopicExchange("at.fhtw.paperlessrest", true, false);
    }

    @Bean
    public Queue fileUploadedQueue() {
        return QueueBuilder.durable("at.fhtw.paperlessrest.domain.model.fileuploaded.batchprocessingworker").build();
    }

    @Bean
    public Binding fileUploadedBinding() {
        return BindingBuilder.bind(fileUploadedQueue()).to(paperlessRestTopic()).with("at.fhtw.paperlessrest.domain.model.fileuploaded");
    }
}
