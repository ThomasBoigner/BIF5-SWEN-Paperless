package at.fhtw.paperlessrest.integrationtest;

import at.fhtw.paperlessrest.application.dtos.FileMetaDataDto;
import at.fhtw.paperlessrest.domain.model.FileMetaData;
import at.fhtw.paperlessrest.domain.model.User;
import at.fhtw.paperlessrest.domain.model.UserRepository;
import at.fhtw.paperlessrest.domain.model.UserToken;
import at.fhtw.paperlessrest.infrastructure.persistence.elasticsearch.FileDocument;
import at.fhtw.paperlessrest.infrastructure.persistence.elasticsearch.FileDocumentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.json.JsonMapper;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Transactional
@ActiveProfiles("postgres")
@AutoConfigureMockMvc
public class IntegrationTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserRepository userRepository;

    private JsonMapper jsonMapper;

    @MockitoBean
    private FileDocumentRepository fileDocumentRepository;

    @BeforeEach
    void setUp() {
        jsonMapper = JsonMapper.builder().build();
    }

    @Test
    void ensureGetAllFileMetaDataWorksProperly() throws Exception {
        // Given
        User user = User.builder()
                .username("test")
                .userToken(new UserToken(UUID.randomUUID()))
                .build();

        FileMetaData fileMetaData = user.uploadFile("test.txt", 100, "test");

        userRepository.save(user);

        FileDocument fileDocument = FileDocument.builder()
                .fileToken(fileMetaData.getFileToken().token())
                .userToken(user.getUserToken().token())
                .fileName(fileMetaData.getFileName())
                .fullText(null)
                .summary(null)
                .build();

        when(fileDocumentRepository.findById(eq(fileMetaData.getFileToken().token()))).thenReturn(Optional.of(fileDocument));

        // Perform
        mockMvc.perform(get("/api/files").with(jwt().jwt(jwt -> jwt.claim("sub", user.getUserToken().token().toString()))).accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(jsonMapper.writeValueAsString(List.of(new FileMetaDataDto(fileMetaData)))));
    }
}
