package at.fhtw.paperlessrest.infrastructure.persistence.jpa;

import at.fhtw.paperlessrest.domain.model.User;
import at.fhtw.paperlessrest.domain.model.UserRepository;
import at.fhtw.paperlessrest.domain.model.UserToken;
import at.fhtw.paperlessrest.infrastructure.persistence.elasticsearch.FileDocumentRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@ActiveProfiles("postgres")
public class JpaUserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @MockitoBean
    private FileDocumentRepository fileDocumentRepository;

    @Test
    public void ensureSaveWorksProperly() {
        // Given
        User user = User.builder()
                .username("test")
                .userToken(new UserToken(UUID.randomUUID()))
                .build();

        // When
        userRepository.save(user);

        // Then
        Optional<User> savedUser = userRepository.findUserByUserToken(user.getUserToken());
        assertThat(savedUser).isPresent();
        assertThat(savedUser.get()).isEqualTo(user);
    }
}
