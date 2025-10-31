package at.fhtw.paperlessrest.infrastructure.configuration;

import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfiguration {
    @Bean
    public TopicExchange topic() {
        return new TopicExchange("at.fhtw.paperlessrest", true, false);
    }
}
