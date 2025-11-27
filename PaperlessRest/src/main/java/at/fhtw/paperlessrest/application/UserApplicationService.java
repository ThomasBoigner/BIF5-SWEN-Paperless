package at.fhtw.paperlessrest.application;

import at.fhtw.paperlessrest.application.commands.RegisterUserCommand;
import at.fhtw.paperlessrest.application.dtos.UserDto;
import at.fhtw.paperlessrest.domain.model.User;
import at.fhtw.paperlessrest.domain.model.UserRepository;
import at.fhtw.paperlessrest.domain.model.UserToken;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@RequiredArgsConstructor

@Service
@Slf4j
@Transactional(readOnly = true)
public class UserApplicationService {
    private final UserRepository userRepository;

    @Transactional(readOnly = false)
    public UserDto registerUser(@Nullable RegisterUserCommand command) {
        Objects.requireNonNull(command, "command must not be null!");
        log.debug("Trying to register user with command {}", command);

        User user = User.builder()
                .userToken(new UserToken(command.token()))
                .username(command.username())
                .build();

        userRepository.save(user);
        log.info("User {} registered successfully!", user);
        return new UserDto(user);
    }
}
