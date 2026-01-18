package at.fhtw.paperlessrest.infrastructure.persistence.elasticsearch;

import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;
import java.util.UUID;

public interface FileDocumentRepository extends ElasticsearchRepository<FileDocument, UUID> {
    void deleteAllByUserToken(UUID userToken);
    @Query(
        """
        {
            "bool": {
                "must": [
                    { "term": { "userToken": "?0" } }
                ],
                "should": [
                    { "wildcard": { "fileName": "*?1*" } },
                    { "match": { "summary": "?1" } },
                    { "match": { "fullText": "?1" } }
                ],
                "minimum_should_match": 1
            }
        }
        """
    )
    List<FileDocument> search(UUID userToken, String query);
}
