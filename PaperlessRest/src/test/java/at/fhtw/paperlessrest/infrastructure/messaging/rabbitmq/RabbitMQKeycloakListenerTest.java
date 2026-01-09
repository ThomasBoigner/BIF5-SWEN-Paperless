package at.fhtw.paperlessrest.infrastructure.messaging.rabbitmq;

import at.fhtw.paperlessrest.application.UserApplicationService;
import at.fhtw.paperlessrest.application.commands.RegisterUserCommand;
import at.fhtw.paperlessrest.infrastructure.messaging.rabbitmq.events.UserDeleted;
import at.fhtw.paperlessrest.infrastructure.messaging.rabbitmq.events.UserRegistered;
import at.fhtw.paperlessrest.infrastructure.messaging.rabbitmq.events.UserRegisteredDetails;
import org.jspecify.annotations.NullUnmarked;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

@NullUnmarked
@ExtendWith(MockitoExtension.class)
public class RabbitMQKeycloakListenerTest {
    private RabbitMQKeycloakListener listener;
    @Mock
    private UserApplicationService userApplicationService;

    @BeforeEach
    void setUp() {
        listener = new RabbitMQKeycloakListener(userApplicationService);
    }

    @Test
    void ensureReceiveUserRegisteredEventWorksProperly() {
        // Given
        UUID userId =  UUID.randomUUID();
        String username = "johndoe";

        UserRegistered event = UserRegistered.builder()
                .userId(userId)
                .details(UserRegisteredDetails.builder()
                        .username(username)
                        .build())
                .build();

        // When
        listener.receiveUserRegisteredEvent(event);

        // Then
        verify(userApplicationService).registerUser(eq(RegisterUserCommand.builder()
                .token(userId)
                .username(username)
                .build())
        );
    }

    @Test
    void ensureReceiveUserDeletedEventWorksProperly() {
        // Given
        UUID userId =  UUID.randomUUID();

        UserDeleted event = UserDeleted.builder()
                .userId(userId)
                .build();

        // When
        listener.receiveUserDeletedEvent(event);

        // Then
        verify(userApplicationService).deleteUser(eq(userId));
    }
}
