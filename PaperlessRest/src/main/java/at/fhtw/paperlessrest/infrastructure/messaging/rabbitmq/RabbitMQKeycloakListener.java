package at.fhtw.paperlessrest.infrastructure.messaging.rabbitmq;

import at.fhtw.paperlessrest.infrastructure.messaging.rabbitmq.events.UserRegistered;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.Nullable;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor

@Slf4j
@Component
public class RabbitMQKeycloakListener {
    @RabbitListener(queues = "KK.EVENT.CLIENT.paperless.SUCCESS.*.REGISTER")
    public void receiveUserRegisteredEvent(@Nullable UserRegistered event) {
        log.info("Received user registered event: {}", event);
    }
}
