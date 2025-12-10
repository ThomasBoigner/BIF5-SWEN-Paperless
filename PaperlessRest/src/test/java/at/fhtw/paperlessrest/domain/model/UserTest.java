package at.fhtw.paperlessrest.domain.model;

import org.jspecify.annotations.NullUnmarked;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@NullUnmarked
public class UserTest {
    @Test
    void ensureGetFileWorksProperly() {
        // Given
        User user = User.builder()
                .username("test")
                .userToken(new UserToken(UUID.randomUUID()))
                .build();

        FileMetaData fileMetaData = user.uploadFile("test.txt", 100, "test");

        // When
        Optional<FileMetaData> result = user.getFile(fileMetaData.getFileToken());

        // Then
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(fileMetaData);
    }

    @Test
    void ensureHasFileWorksProperly() {
        // Given
        User user = User.builder()
                .username("test")
                .userToken(new UserToken(UUID.randomUUID()))
                .build();

        FileMetaData fileMetaData = user.uploadFile("test.txt", 100, "test");

        // When
        boolean result = user.hasFile(fileMetaData.getFileToken());

        // Then
        assertThat(result).isTrue();
    }

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

    @Test
    void ensureAddFullTextToFileWorksProperly() {
        // Given
        User user = User.builder()
                .username("test")
                .userToken(new UserToken(UUID.randomUUID()))
                .build();

        FileMetaData fileMetaData = user.uploadFile("test.txt", 100, "test");

        String fullText = "full text";

        // When
        user.addFullTextToFile(fileMetaData.getFileToken(), fullText);

        // Then
        assertThat(fileMetaData.getFullText()).isEqualTo(fullText);
    }

    @Test
    void ensureRemoveFileWorksProperly() {
        // Given
        User user = User.builder()
                .username("test")
                .userToken(new UserToken(UUID.randomUUID()))
                .build();

        FileMetaData fileMetaData = user.uploadFile("test.txt", 100, "test");

        // When
        user.removeFile(fileMetaData.getFileToken());

        // Then
        assertThat(user.getFiles()).hasSize(0);
    }
}
