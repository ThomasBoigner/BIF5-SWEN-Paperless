package at.fhtw.paperlessrest.infrastructure.search.elasticsearch;

import lombok.Builder;

import java.util.UUID;

@Builder
public record FullTextDocument(UUID fileToken, String fileName, String fullText) { }
