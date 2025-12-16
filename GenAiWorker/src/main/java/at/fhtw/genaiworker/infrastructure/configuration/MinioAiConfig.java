package at.fhtw.genaiworker.infrastructure.configuration;

import io.minio.MinioClient;
import org.springframework.beans.factory.annotation.Value;import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MinioAiConfig {

    @Bean
    public MinioClient minioClient(
            @Value("${minio.url}") String url,
            @Value("${minio.access.key}") String accessKey,
            @Value("${minio.secret.key}") String secretKey) {

        return MinioClient.builder()
                .endpoint(url)
                .credentials(accessKey, secretKey)
                .build();
    }
}
