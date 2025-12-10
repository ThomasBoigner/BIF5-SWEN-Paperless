package at.fhtw.paperlessrest.application;

import at.fhtw.paperlessrest.application.commands.AddFullTextCommand;
import at.fhtw.paperlessrest.application.commands.UpdateFileCommand;
import at.fhtw.paperlessrest.application.commands.UploadFileCommand;
import at.fhtw.paperlessrest.application.dtos.FileMetaDataDto;
import at.fhtw.paperlessrest.domain.model.*;
import org.jspecify.annotations.NullUnmarked;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.InputStreamResource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@NullUnmarked
@ExtendWith(MockitoExtension.class)
public class FileMetaDataApplicationServiceTest {
    private FileMetaDataApplicationService fileMetaDataApplicationService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private FileMetaDataRepository fileMetaDataRepository;
    @Mock
    private FileService fileService;
    @Mock
    private FileMetaDataEventPublisher fileMetaDataEventPublisher;

    @BeforeEach
    void setUp() {
        fileMetaDataApplicationService = new FileMetaDataApplicationService(fileMetaDataRepository, userRepository, fileMetaDataEventPublisher, fileService);
    }

    @Test
    void ensureGetAllFileMetaDataWorksProperly() {
        // Given
        User user = User.builder()
                .username("test")
                .userToken(new UserToken(UUID.randomUUID()))
                .build();

        FileMetaData fileMetaData = user.uploadFile("test.txt", 100, "test");

        when(userRepository.findUserByUserToken(eq(user.getUserToken()))).thenReturn(Optional.of(user));

        // When
        List<FileMetaDataDto> result = fileMetaDataApplicationService.getAllFileMetaData(user.getUserToken().token());

        // Then
        assertThat(result).hasSize(1);
        assertThat(result).contains(new FileMetaDataDto(fileMetaData));
    }

    @Test
    void ensureGetFileByTokenWorksProperly() {
        // Given
        User user = User.builder()
                .username("test")
                .userToken(new UserToken(UUID.randomUUID()))
                .build();

        FileMetaData fileMetaData = user.uploadFile("test.txt", 100, "test");

        when(userRepository.findUserByUserToken(eq(user.getUserToken()))).thenReturn(Optional.of(user));

        // When
        Optional<FileMetaDataDto> result = fileMetaDataApplicationService.getFileMetaData(user.getUserToken().token(), fileMetaData.getFileToken().token());

        // Then
        assertThat(result).isPresent();
        assertThat(result).isEqualTo(Optional.of(new FileMetaDataDto(fileMetaData)));
    }

    @Test
    void ensureDownloadFileWorksProperly() {
        // Given
        User user = User.builder()
                .username("test")
                .userToken(new UserToken(UUID.randomUUID()))
                .build();

        FileMetaData fileMetaData = user.uploadFile("test.txt", 100, "test");

        InputStreamResource inputStreamResource = mock(InputStreamResource.class);

        when(userRepository.findUserByUserToken(eq(user.getUserToken()))).thenReturn(Optional.of(user));
        when(fileService.downloadFile(eq(fileMetaData.getFileToken().token()))).thenReturn(inputStreamResource);

        // When
        Optional<InputStreamResource> result = fileMetaDataApplicationService.downloadFile(user.getUserToken().token(), fileMetaData.getFileToken().token());

        // Then
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(inputStreamResource);
    }

    @Test
    void ensureUploadFileWorksProperly() throws IOException {
        // Given
        String fileName = "test.pdf";

        MultipartFile file = Mockito.mock(MultipartFile.class);

        UploadFileCommand command = UploadFileCommand.builder()
                .description("test")
                .build();

        UUID userToken = UUID.randomUUID();

        User user = User.builder()
                .username("test")
                .userToken(new UserToken(UUID.randomUUID()))
                .build();

        when(userRepository.findUserByUserToken(eq(new UserToken(userToken)))).thenReturn(Optional.of(user));
        when(file.getContentType()).thenReturn("application/pdf");
        when(file.getOriginalFilename()).thenReturn(fileName);
        when(file.getSize()).thenReturn(8L);

        // When
        FileMetaDataDto result = fileMetaDataApplicationService.uploadFile(userToken, file, command);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.fileName()).isEqualTo(fileName);
        assertThat(result.fileSize()).isEqualTo(8L);
        assertThat(result.description()).isEqualTo(command.description());
    }

    @Test
    void ensureUploadFileThrowsExceptionWhenNonPdfIsUploaded() {
        // Given
        MultipartFile file = Mockito.mock(MultipartFile.class);

        UploadFileCommand command = UploadFileCommand.builder()
                .description("test")
                .build();

        when(file.getContentType()).thenReturn("plain/text");

        // When
        assertThrows(IllegalArgumentException.class,
                () -> fileMetaDataApplicationService.uploadFile(UUID.randomUUID(), file, command));
    }

    @Test
    void ensureUploadFileThrowsExceptionWhenUserCantBeFound() throws IOException {
        // Given
        MultipartFile file = Mockito.mock(MultipartFile.class);

        UploadFileCommand command = UploadFileCommand.builder()
                .description("test")
                .build();

        UUID userToken = UUID.randomUUID();

        when(userRepository.findUserByUserToken(eq(new UserToken(userToken)))).thenReturn(Optional.empty());

        // When
        assertThrows(IllegalArgumentException.class,
                () -> fileMetaDataApplicationService.uploadFile(userToken, file, command));
    }

    @Test
    void ensureAddFullTextWorksProperly() {
        // Given
        String fullText = "Full Text";

        User user = User.builder()
                .username("test")
                .userToken(new UserToken(UUID.randomUUID()))
                .build();

        FileMetaData fileMetaData = user.uploadFile("test.txt", 100, "test");

        when(userRepository.findUserByUserToken(eq(user.getUserToken()))).thenReturn(Optional.of(user));

        AddFullTextCommand command = AddFullTextCommand.builder()
                .fullText(fullText)
                .userToken(user.getUserToken().token())
                .fileToken(fileMetaData.getFileToken().token())
                .build();

        // When
        fileMetaDataApplicationService.addFullText(command);

        // Then
        assertThat(fileMetaData.getFullText()).isEqualTo(fullText);
    }

    @Test
    void ensureAddFullTextThrowsExceptionWhenFileCanNotBeFound() {
        // Given
        String fullText = "Full Text";

        User user = User.builder()
                .username("test")
                .userToken(new UserToken(UUID.randomUUID()))
                .build();

        FileMetaData fileMetaData = user.uploadFile("test.txt", 100, "test");

        when(userRepository.findUserByUserToken(eq(user.getUserToken()))).thenReturn(Optional.empty());

        AddFullTextCommand command = AddFullTextCommand.builder()
                .fullText(fullText)
                .userToken(user.getUserToken().token())
                .fileToken(fileMetaData.getFileToken().token())
                .build();

        // When
        fileMetaDataApplicationService.addFullText(command);

        // Then
        assertThat(fileMetaData.getFullText()).isEqualTo(null);
    }

    @Test
    void ensureUpdateFileWorksProperly() {
        // Given
        FileMetaData fileMetaData = FileMetaData.builder()
                .fileName("test.txt")
                .fileSize(100)
                .description("test")
                .build();

        UpdateFileCommand command = UpdateFileCommand.builder()
                .description("updated")
                .build();

        when(fileMetaDataRepository.findFileMetaDataByFileToken(eq(fileMetaData.getFileToken())))
                .thenReturn(Optional.of(fileMetaData));
        when(fileMetaDataRepository.save(any(FileMetaData.class)))
                .thenAnswer(invocationOnMock -> invocationOnMock.getArguments()[0]);

        // When
        FileMetaDataDto result = fileMetaDataApplicationService.updateFileMetaData(fileMetaData.getFileToken().token(), command);

        // then
        assertThat(result).isNotNull();
        assertThat(result.description()).isEqualTo(command.description());
    }

    @Test
    void ensureUpdateFileThrowsExceptionWhenFileCanNotBeFound() {
        // Given
        FileMetaData fileMetaData = FileMetaData.builder()
                .fileName("test.txt")
                .fileSize(100)
                .description("test")
                .build();

        UpdateFileCommand command = UpdateFileCommand.builder()
                .description("updated")
                .build();

        when(fileMetaDataRepository.findFileMetaDataByFileToken(eq(fileMetaData.getFileToken())))
                .thenReturn(Optional.empty());

        // When
        assertThrows(IllegalArgumentException.class,
                () -> fileMetaDataApplicationService.updateFileMetaData(fileMetaData.getFileToken().token(), command));
    }

    @Test
    void ensureDeleteFileWorksProperly() {
        // Given
        User user = User.builder()
                .username("test")
                .userToken(new UserToken(UUID.randomUUID()))
                .build();

        FileMetaData fileMetaData = user.uploadFile("test.txt", 100, "test");

        when(userRepository.findUserByUserToken(eq(user.getUserToken()))).thenReturn(Optional.of(user));

        // When
        fileMetaDataApplicationService.deleteFileMetaData(user.getUserToken().token(), fileMetaData.getFileToken().token());
    }
}
