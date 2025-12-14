package at.fhtw.paperlessrest.application;

import at.fhtw.paperlessrest.application.commands.RegisterUserCommand;
import at.fhtw.paperlessrest.application.dtos.UserDto;
import at.fhtw.paperlessrest.domain.model.UserRepository;
import org.jspecify.annotations.NullUnmarked;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@NullUnmarked
@ExtendWith(MockitoExtension.class)
public class UserApplicationServiceTest {
    private UserApplicationService userApplicationService;
    @Mock
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userApplicationService = new UserApplicationService(userRepository);
    }

    @Test
    void ensureRegisterUserWorksProperly() {
        // Given
        UUID userToken = UUID.randomUUID();
        String username = "johndoe";

        RegisterUserCommand command = RegisterUserCommand.builder()
                .username(username)
                .token(userToken)
                .build();

        // When
        UserDto userDto = userApplicationService.registerUser(command);

        // Then
        assertThat(userDto).isNotNull();
        assertThat(userDto.username()).isEqualTo(username);
        assertThat(userDto.token()).isEqualTo(userToken);
    }

    @Test
    void ensureDeleteUserWorksProperly() {
        // Given
        UUID userToken = UUID.randomUUID();

        // When
        userApplicationService.deleteUser(userToken);
    }
}
