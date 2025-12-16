package at.fhtw.ocrworker.infrastructure.configuration;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import org.jspecify.annotations.Nullable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.URISyntaxException;

@Configuration
public class ElasticsearchConfiguration {
    @Value("${elasticsearch.url}")
    @Nullable
    private String elasticsearchUrl;

    @Bean
    public ElasticsearchClient elasticsearchClient() {
        return ElasticsearchClient.of(b -> b.host(elasticsearchUrl));
    }
}
