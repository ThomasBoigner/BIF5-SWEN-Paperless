package at.fhtw.ocrworker.infrastructure.search.elasticsearch;

import at.fhtw.ocrworker.application.SearchService;
import co.elastic.clients.elasticsearch.ElasticsearchClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.UUID;

@RequiredArgsConstructor

@Slf4j
@Service
public class ElasticsearchSearchService implements SearchService {
    private final ElasticsearchClient elasticsearchClient;

    @Override
    public void saveFullText(UUID userToken, UUID fileToken, String fileName, String fulltext) {
        try {
            elasticsearchClient.index(i -> i
                    .index("paperless-documents-%s".formatted(userToken))
                    .id(fileToken.toString())
                    .document(FullTextDocument.builder()
                            .fileToken(fileToken)
                            .fileName(fileName)
                            .fullText(fulltext)
                            .build())
            );
        } catch (IOException e) {
            log.error("Could not save full text to Elasticsearch index, message: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
