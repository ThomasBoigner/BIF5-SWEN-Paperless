package at.fhtw.genaiworker.infrastructure.configuration;

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
    public TopicExchange paperlessRestTopic() {
        return new TopicExchange("at.fhtw.paperlessrest", true, false);
    }

    @Bean
    public Queue fullTextAddedQueue() {
        return QueueBuilder.durable("at.fhtw.paperlessrest.domain.model.fulltextadded").build();
    }

    @Bean
    public Binding fullTextAddedBinding() {
        return BindingBuilder.bind(fullTextAddedQueue()).to(paperlessRestTopic()).with("at.fhtw.paperlessrest.domain.model.fulltextadded");
    }
}
