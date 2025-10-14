package at.fhtw.paperlessrest.presentation;

import at.fhtw.paperlessrest.application.FileMetaDataApplicationService;
import at.fhtw.paperlessrest.application.dtos.FileMetaDataDto;
import at.fhtw.paperlessrest.domain.model.FileMetaData;
import at.fhtw.paperlessrest.domain.model.FileName;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.jspecify.annotations.NullUnmarked;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
    private FileMetaDataDto fileMetaDataDto;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new FileRestController(fileMetaDataApplicationService)).build();

        objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

        fileMetaDataDto = new FileMetaDataDto(
                FileMetaData.builder()
                .fileName(FileName.builder()
                        .name("test")
                        .fileExtension("txt")
                        .build())
                .fileSize(1000)
                .description("test")
                .build()
        );
    }

    @Test
    void ensureGetAllFileMetaDataWorksProperly() throws Exception {
        // When
        when(fileMetaDataApplicationService.getAllFileMetaData()).thenReturn(List.of(fileMetaDataDto));

        // Perform
        mockMvc.perform(get("/api/files").accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(objectMapper.writeValueAsString(List.of(fileMetaDataDto))));
    }

    @Test
    void ensureGetAllFileMetaDataReturnsNotFoundIfDataCouldNotBeFound() throws Exception {
        // When
        when(fileMetaDataApplicationService.getAllFileMetaData()).thenReturn(List.of());

        // Perform
        mockMvc.perform(get("/api/files").accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent());
    }
}
