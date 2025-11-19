package at.fhtw.paperlessrest.infrastructure.persistence.jpa;

import at.fhtw.paperlessrest.domain.model.FileMetaData;
import at.fhtw.paperlessrest.domain.model.FileMetaDataRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@ActiveProfiles("postgres")
public class JpaFileMetaDataRepositoryTest {
    @Autowired
    private FileMetaDataRepository fileMetaDataRepository;

    @Test
    void ensureSaveFileMetaDataWorksProperly() {
        // Given
        FileMetaData fileMetaData = FileMetaData.builder()
                .fileName("test.txt")
                .fileSize(100)
                .description("test")
                .build();

        // When
        fileMetaDataRepository.save(fileMetaData);

        // Then
        Optional<FileMetaData> savedFileMetaData = fileMetaDataRepository.findFileMetaDataByFileToken(fileMetaData.getFileToken());
        assertThat(savedFileMetaData).isPresent();
        assertThat(savedFileMetaData.get()).isEqualTo(fileMetaData);
    }
}
