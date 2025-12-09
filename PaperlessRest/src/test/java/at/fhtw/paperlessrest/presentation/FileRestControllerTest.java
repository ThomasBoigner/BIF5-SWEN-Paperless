package at.fhtw.paperlessrest.presentation;

import at.fhtw.paperlessrest.application.FileMetaDataApplicationService;
import at.fhtw.paperlessrest.application.FileService;
import at.fhtw.paperlessrest.application.commands.UpdateFileCommand;
import at.fhtw.paperlessrest.application.commands.UploadFileCommand;
import at.fhtw.paperlessrest.application.dtos.FileMetaDataDto;
import at.fhtw.paperlessrest.domain.model.FileMetaData;
import org.jspecify.annotations.NullUnmarked;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.multipart.MultipartFile;
import tools.jackson.databind.json.JsonMapper;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@NullUnmarked
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
public class FileRestControllerTest {
    private MockMvc mockMvc;

    @Mock
    private FileMetaDataApplicationService fileMetaDataApplicationService;
    @Mock
    private FileService fileService;
    private FileMetaDataDto fileMetaDataDto;
    private JsonMapper jsonMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(new FileRestController(fileMetaDataApplicationService))
                .setCustomArgumentResolvers(new PrincipalDetailsArgumentResolver())
                .build();

        jsonMapper = JsonMapper.builder()
                .build();

        fileMetaDataDto = new FileMetaDataDto(
                FileMetaData.builder()
                .fileName("test.txt")
                .fileSize(100)
                .description("test")
                .build()
        );
    }

    @Test
    void ensureGetAllFileMetaDataWorksProperly() throws Exception {
        // When
        when(fileMetaDataApplicationService.getAllFileMetaData(any(UUID.class))).thenReturn(List.of(fileMetaDataDto));

        // Perform
        mockMvc.perform(get("/api/files").accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(jsonMapper.writeValueAsString(List.of(fileMetaDataDto))));
    }

    @Test
    void ensureGetAllFileMetaDataReturnsNoContentIfDataCouldNotBeFound() throws Exception {
        // When
        when(fileMetaDataApplicationService.getAllFileMetaData(any(UUID.class))).thenReturn(List.of());

        // Perform
        mockMvc.perform(get("/api/files").accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    void ensureGetFileMetaDataWorksProperly() throws Exception {
        // When
        when(fileMetaDataApplicationService.getFileMetaData(any(UUID.class), eq(fileMetaDataDto.fileToken()))).thenReturn(Optional.of(fileMetaDataDto));

        // Perform
        mockMvc.perform(get("/api/files/%s".formatted(fileMetaDataDto.fileToken())).accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(jsonMapper.writeValueAsString(fileMetaDataDto)));
    }

    @Test
    void ensureGetFileMetaDataReturnsNotFoundIfDataCouldNotBeFound() throws Exception {
        // When
        when(fileMetaDataApplicationService.getFileMetaData(any(UUID.class), eq(fileMetaDataDto.fileToken()))).thenReturn(Optional.empty());

        // Perform
        mockMvc.perform(get("/api/files/%s".formatted(fileMetaDataDto.fileToken())).accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void ensureGetFileContentWorksProperly() throws Exception {
        // When
        when(fileMetaDataApplicationService.downloadFile(any(UUID.class), eq(fileMetaDataDto.fileToken()))).thenReturn(Optional.of(mock(InputStreamResource.class)));

        // Perform
        mockMvc.perform(get("/api/files/%s/download".formatted(fileMetaDataDto.fileToken())).accept(MediaType.APPLICATION_PDF_VALUE))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void ensureUploadFileWorksProperly() throws Exception {
        // Given
        MockMultipartFile filePart = new MockMultipartFile(
                "file",
                "test.txt",
                MediaType.TEXT_PLAIN_VALUE,
                "This is a test file".getBytes(StandardCharsets.UTF_8)
        );

        UploadFileCommand command = UploadFileCommand.builder()
                .description("test")
                .build();
        MockMultipartFile commandPart = new MockMultipartFile(
                "command",
                "",
                MediaType.APPLICATION_JSON_VALUE,
                jsonMapper.writeValueAsBytes(command)
        );

        when(fileMetaDataApplicationService.uploadFile(any(UUID.class), any(MultipartFile.class), eq(command))).thenReturn(fileMetaDataDto);

        // Perform
        mockMvc.perform(multipart("/api/files")
                        .file(filePart)
                        .file(commandPart)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(jsonMapper.writeValueAsString(fileMetaDataDto)));
    }

    @Test
    void ensureUpdateFileMetaDataWorksProperly() throws Exception {
        // Given
        UpdateFileCommand command = UpdateFileCommand.builder()
                .description("test")
                .build();

        when(fileMetaDataApplicationService.updateFileMetaData(eq(fileMetaDataDto.fileToken()), eq(command))).thenReturn(fileMetaDataDto);

        // Perform
        mockMvc.perform(put("/api/files/%s".formatted(fileMetaDataDto.fileToken()))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(command)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(jsonMapper.writeValueAsString(fileMetaDataDto)));
    }

    @Test
    void ensureDeleteFileMetaDataWorksProperly() throws Exception {
        // Perform
        mockMvc.perform(delete("/api/files/%s".formatted(fileMetaDataDto.fileToken()))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }
}
