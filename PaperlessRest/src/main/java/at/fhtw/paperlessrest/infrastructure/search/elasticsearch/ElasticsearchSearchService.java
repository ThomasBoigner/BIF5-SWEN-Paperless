package at.fhtw.paperlessrest.infrastructure.search.elasticsearch;

import at.fhtw.paperlessrest.application.SearchService;
import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
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
        String wildCardQuery = "*".concat(query).concat("*");

        try {
            SearchResponse<FullTextDocument> searchResponse = elasticsearchClient.search(s -> s
                    .index("paperless-documents-%s".formatted(userToken))
                    .query(q -> q
                            .bool(b -> b
                                    .should(sh -> sh
                                            .wildcard(t -> t
                                                    .field("fileName.keyword")
                                                    .value(wildCardQuery)
                                            )
                                    )
                                    .should(sh -> sh
                                            .match(t -> t
                                                    .field("fullText")
                                                    .query(query)
                                            )
                                    )
                            )
                    ),
                    FullTextDocument.class
            );

            return searchResponse.hits().hits().stream()
                    .map(Hit::source)
                    .filter(Objects::nonNull)
                    .map(FullTextDocument::fileToken)
                    .toList();
        } catch (IOException e) {
            log.error("Could not get full text with user token {} and query {}, message: {}", userToken, query, e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
