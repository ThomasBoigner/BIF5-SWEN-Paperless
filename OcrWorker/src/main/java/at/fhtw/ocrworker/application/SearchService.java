package at.fhtw.ocrworker.application;

import java.util.UUID;

public interface SearchService {
    void saveFullText(UUID fileToken, String fulltext);
}
