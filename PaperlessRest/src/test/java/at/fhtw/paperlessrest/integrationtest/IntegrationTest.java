package at.fhtw.paperlessrest.integrationtest;

import at.fhtw.paperlessrest.application.dtos.FileMetaDataDto;
import at.fhtw.paperlessrest.domain.model.FileMetaData;
import at.fhtw.paperlessrest.domain.model.User;
import at.fhtw.paperlessrest.domain.model.UserRepository;
import at.fhtw.paperlessrest.domain.model.UserToken;
import at.fhtw.paperlessrest.infrastructure.persistence.elasticsearch.FileDocumentRepository;
import at.fhtw.paperlessrest.presentation.FileRestController;
import at.fhtw.paperlessrest.presentation.PrincipalDetailsArgumentResolver;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.json.JsonMapper;

import java.util.List;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Transactional
@ActiveProfiles("postgres")
@Slf4j
public class IntegrationTest {
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private FileRestController fileRestController;
    private JsonMapper jsonMapper;

    @MockitoBean
    private FileDocumentRepository fileDocumentRepository;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(fileRestController)
                .setCustomArgumentResolvers(new PrincipalDetailsArgumentResolver())
                .build();

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

        // Perform
        mockMvc.perform(get("/api/files").accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(jsonMapper.writeValueAsString(List.of(new FileMetaDataDto(fileMetaData)))));
    }
}
