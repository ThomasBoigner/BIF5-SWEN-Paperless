package at.fhtw.paperlessrest.infrastructure.persistence.elasticsearch;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.UUID;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(indexName = "paperless-file-documents")
public class FileDocument {
    @Id
    private UUID fileToken;
    @Field(type = FieldType.Keyword)
    private UUID userToken;
    @Field(type = FieldType.Keyword)
    private String fileName;
    @Nullable
    private String fullText;
    @Nullable
    private String summary;
}
