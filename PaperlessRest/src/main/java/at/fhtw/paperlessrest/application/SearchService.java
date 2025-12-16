package at.fhtw.paperlessrest.application;

import java.util.UUID;

public interface SearchService {
    void deleteFullText(UUID fileToken);
}
