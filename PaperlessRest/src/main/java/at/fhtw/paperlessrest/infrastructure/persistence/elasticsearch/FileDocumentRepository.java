package at.fhtw.paperlessrest.infrastructure.persistence.elasticsearch;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;
import java.util.UUID;

public interface FileDocumentRepository extends ElasticsearchRepository<FileDocument, UUID> {
    void deleteAllByUserToken(UUID userToken);
    List<FileDocument> findFileDocumentByUserTokenAndFileNameLikeOrSummaryLikeOrFullTextLike(
            UUID userToken,
            String filename,
            String summary,
            String fullText
    );
}
