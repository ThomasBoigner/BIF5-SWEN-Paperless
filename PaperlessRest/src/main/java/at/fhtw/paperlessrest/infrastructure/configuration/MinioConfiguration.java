package at.fhtw.paperlessrest.infrastructure.configuration;

import io.minio.MinioClient;
import org.jspecify.annotations.Nullable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MinioConfiguration {
    @Value("${minio.url}")
    @Nullable
    private String minioUrl;

    @Value("${minio.access.key}")
    @Nullable
    private String minioAccessKey;

    @Value("${minio.secret.key}")
    @Nullable
    private String minioSecretKey;

    @Bean
    public MinioClient minioClient() {
        if (minioUrl == null || minioAccessKey == null || minioSecretKey == null) {
            throw new IllegalArgumentException("minioUrl or minioAccessKey or minioSecretKey is not set");
        }
        return MinioClient.builder()
                .endpoint(minioUrl)
                .credentials(minioAccessKey, minioSecretKey)
                .build();
    }
}
