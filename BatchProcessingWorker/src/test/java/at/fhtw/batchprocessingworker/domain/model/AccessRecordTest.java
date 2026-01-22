package at.fhtw.batchprocessingworker.domain.model;

import org.jspecify.annotations.NullUnmarked;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@NullUnmarked
public class AccessRecordTest {
    @Test
    void ensureCreateAccessRecordWorksProperly() {
        // Given
        UserToken userToken = new UserToken(UUID.randomUUID());
        FileToken fileToken = new FileToken(UUID.randomUUID());

        // When
        AccessRecord accessRecord = AccessRecord.builder()
                .fileToken(fileToken)
                .userToken(userToken)
                .build();

        // Then
        assertThat(accessRecord).isNotNull();
        assertThat(accessRecord.getAccessRecordToken()).isNotNull();
        assertThat(accessRecord.getUserToken()).isEqualTo(userToken);
        assertThat(accessRecord.getFileToken()).isEqualTo(fileToken);
        assertThat(accessRecord.getNumberOfAccesses()).isBetween(0, 10);
    }
}
