package at.fhtw.paperlessrest.domain.model;

import org.jspecify.annotations.NullUnmarked;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@NullUnmarked
public class UserTest {
    @Test
    void ensureUploadFileWorksProperly() {
        // Given
        User user = User.builder()
                .username("test")
                .userToken(new UserToken(UUID.randomUUID()))
                .build();

        String filename = "filename";
        long filesize = 10L;
        String description = "description";

        // When
        FileMetaData fileMetadata = user.uploadFile(filename, filesize, description);

        // Then
        assertThat(user.getFiles()).hasSize(1);
        assertThat(user.getFileUploadedEvents()).hasSize(1);
        assertThat(fileMetadata.getFileName()).isEqualTo(filename);
        assertThat(fileMetadata.getFileSize()).isEqualTo(filesize);
        assertThat(fileMetadata.getDescription()).isEqualTo(description);
    }
}
