package at.fhtw.paperlessrest.infrastructure.messaging.rabbitmq;

import at.fhtw.paperlessrest.application.UserApplicationService;
import at.fhtw.paperlessrest.application.commands.RegisterUserCommand;
import at.fhtw.paperlessrest.infrastructure.messaging.rabbitmq.events.UserRegistered;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.Nullable;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Objects;

@RequiredArgsConstructor

@Slf4j
@Component
public class RabbitMQKeycloakListener {
    private final UserApplicationService userApplicationService;

    @RabbitListener(queues = "KK.EVENT.CLIENT.paperless.SUCCESS.*.REGISTER")
    public void receiveUserRegisteredEvent(@Nullable UserRegistered event) {
        if (Objects.isNull(event)) {
            log.warn("Received event was null!");
            return;
        }
        log.trace("Received user registered event: {}", event);
        userApplicationService.registerUser(RegisterUserCommand.builder()
                        .token(event.userId())
                        .username(event.details().username())
                .build());
    }
}
