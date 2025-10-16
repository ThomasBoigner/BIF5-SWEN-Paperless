package at.fhtw.paperlessrest.application;

import at.fhtw.paperlessrest.application.commands.UploadFileCommand;
import at.fhtw.paperlessrest.application.dtos.FileMetaDataDto;
import at.fhtw.paperlessrest.domain.model.FileMetaData;
import at.fhtw.paperlessrest.domain.model.FileMetaDataRepository;
import org.jspecify.annotations.NullUnmarked;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@NullUnmarked
@ExtendWith(MockitoExtension.class)
public class FileMetaDataApplicationServiceTest {
    private FileMetaDataApplicationService fileMetaDataApplicationService;
    @Mock
    private FileMetaDataRepository fileMetaDataRepository;

    @BeforeEach
    void setUp() {
        fileMetaDataApplicationService = new FileMetaDataApplicationService(fileMetaDataRepository);
    }

    @Test
    void ensureGetAllFileMetaDataWorksProperly() {
        // Given
        FileMetaData fileMetaData = FileMetaData.builder()
                .fileName("test.txt")
                .fileSize(1000)
                .description("test")
                .build();

        when(fileMetaDataRepository.findAll()).thenReturn(List.of(fileMetaData));

        // When
        List<FileMetaDataDto> result = fileMetaDataApplicationService.getAllFileMetaData();

        // Then
        assertThat(result).hasSize(1);
        assertThat(result).contains(new FileMetaDataDto(fileMetaData));
    }

    @Test
    void ensureUploadFIleWorksProperly() {
        // Given
        String fileName = "test.txt";
        long fileSize = 1000;

        MultipartFile file = Mockito.mock(MultipartFile.class);

        UploadFileCommand command = UploadFileCommand.builder()
                .description("test")
                .build();

        when(file.getOriginalFilename()).thenReturn(fileName);
        when(file.getSize()).thenReturn(fileSize);

        // When
        FileMetaDataDto result = fileMetaDataApplicationService.uploadFile(file, command);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.fileName()).isEqualTo("test.txt");
        assertThat(result.fileSize()).isEqualTo(fileSize);
        assertThat(result.description()).isEqualTo(command.description());
    }
}
