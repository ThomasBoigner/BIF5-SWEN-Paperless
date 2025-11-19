package at.fhtw.paperlessrest.domain.model;

import org.jspecify.annotations.NullUnmarked;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@NullUnmarked
public class FileMetaDataTest {
    @Test
    void ensureAddFullTextWorksProperly() {
        // Given
        String fullText = "Full Text";

        FileMetaData fileMetaData = FileMetaData.builder()
                .fileName("test.txt")
                .fileSize(100)
                .description("test")
                .build();

        // When
        fileMetaData.addFullText(fullText);

        // Then
        assertThat(fileMetaData.getFullText()).isEqualTo(fullText);
        assertThat(fileMetaData.getFullTextAddedEvents()).hasSize(1);
    }
}
