package at.fhtw.paperlessrest.infrastructure.persistence.elasticsearch;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

public interface FileDocumentRepository extends ElasticsearchRepository<FileDocument, UUID> {
}
