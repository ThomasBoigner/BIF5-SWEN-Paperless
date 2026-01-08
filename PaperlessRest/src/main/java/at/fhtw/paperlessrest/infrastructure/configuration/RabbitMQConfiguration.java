package at.fhtw.paperlessrest.infrastructure.configuration;

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
    public TopicExchange ocrWorkerTopic() {
        return new TopicExchange("at.fhtw.ocrworker", true, false);
    }

    @Bean
    public TopicExchange genAiWorkerTopic() {
        return new TopicExchange("at.fhtw.genaiworker", true, false);
    }

    @Bean
    public TopicExchange keycloakTopic() {
        return new TopicExchange("KK.EVENT", true, false);
    }

    @Bean
    public Queue userRegisteredQueue() {
        return QueueBuilder.durable("KK.EVENT.CLIENT.paperless.SUCCESS.*.REGISTER").build();
    }

    @Bean
    public Queue userDeletedQueue() {
        return QueueBuilder.durable("KK.EVENT.CLIENT.paperless.SUCCESS.*.DELETE_ACCOUNT").build();
    }

    @Bean
    public Binding userRegisteredBinding() {
        return BindingBuilder.bind(userRegisteredQueue()).to(keycloakTopic()).with("KK.EVENT.CLIENT.paperless.SUCCESS.*.REGISTER");
    }

    @Bean
    public Binding userDeletedBinding() {
        return BindingBuilder.bind(userDeletedQueue()).to(keycloakTopic()).with("KK.EVENT.CLIENT.paperless.SUCCESS.*.DELETE_ACCOUNT");
    }

    @Bean
    public Queue textExtractedQueue() {
        return QueueBuilder.durable("at.fhtw.ocrworker.domain.model.textextracted").build();
    }

    @Bean
    public Binding textExtractedBinding() {
        return BindingBuilder.bind(textExtractedQueue()).to(ocrWorkerTopic()).with("at.fhtw.ocrworker.domain.model.textextracted");
    }

    @Bean
    public Queue summaryCreatedQueue() {
        return QueueBuilder.durable("at.fhtw.genaiworker.domain.model.summarycreated").build();
    }

    @Bean
    public Binding summaryCreatedBinding() {
        return BindingBuilder.bind(summaryCreatedQueue()).to(genAiWorkerTopic()).with("at.fhtw.genaiworker.domain.model.summarycreated");
    }
}
