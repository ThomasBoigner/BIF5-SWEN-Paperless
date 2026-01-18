package at.fhtw.paperlessrest;

import at.fhtw.paperlessrest.infrastructure.persistence.elasticsearch.FileDocumentRepository;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest
public class PaperlessRestApplicationTest {
    @MockitoBean
    private FileDocumentRepository fileDocumentRepository;

    @Test
    void contextLoads() {
    }
}
