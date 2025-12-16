package at.fhtw.genaiworker.infrastructure;

import io.minio.MinioClient;
import io.minio.GetObjectArgs;
import io.minio.PutObjectArgs;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

@Component
public class MinioAdapter {

    private final MinioClient minioClient;

    public MinioAdapter(MinioClient minioClient) {
        this.minioClient = minioClient;
    }

    public String fetchOcrText(String bucket, String objectKey) throws Exception {
        try (var stream = minioClient.getObject(GetObjectArgs.builder()
                .bucket(bucket)
                .object(objectKey)
                .build())) {
            return new String(stream.readAllBytes(), StandardCharsets.UTF_8);
        }
    }

    public void saveSummary(String bucket, String objectKey, String summary) throws Exception {
        try (var stream = new ByteArrayInputStream(summary.getBytes(StandardCharsets.UTF_8))) {
            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(bucket)
                    .object(objectKey)
                    .stream(stream, stream.available(), -1)
                    .contentType("application/json")
                    .build());
        }
    }
}