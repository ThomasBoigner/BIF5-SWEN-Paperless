package at.fhtw.paperlessrest.infrastructure.persistence.elasticsearch;

import lombok.Builder;
import lombok.Getter;
import org.jspecify.annotations.Nullable;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.util.UUID;

@Builder
@Getter
@Document(indexName = "paperless-file-documents")
public class FileDocument {
    @Id
    private UUID fileToken;
    private String fileName;
    @Nullable
    private String fullText;
    @Nullable
    private String summary;
}
