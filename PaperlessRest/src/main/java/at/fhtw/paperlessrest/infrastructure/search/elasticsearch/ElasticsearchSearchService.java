package at.fhtw.paperlessrest.infrastructure.search.elasticsearch;

import at.fhtw.paperlessrest.application.SearchService;
import co.elastic.clients.elasticsearch.ElasticsearchClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor

@Slf4j
@Service
public class ElasticsearchSearchService implements SearchService {
    private final ElasticsearchClient elasticsearchClient;

    @Override
    public void deleteFullText(UUID userToken, UUID fileToken) {
        try {
            elasticsearchClient.delete(i -> i
                    .index("paperless-documents-%s".formatted(userToken))
                    .id(fileToken.toString())
            );
        } catch (IOException e) {
            log.error("Could not delete full text with token {}, message: {}", fileToken, e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<UUID> queryFileMetaData(UUID userToken, String query) {
        return List.of();
    }
}
