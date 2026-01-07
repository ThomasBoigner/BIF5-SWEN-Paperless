package at.fhtw.paperlessrest.application;

import java.util.List;
import java.util.UUID;

public interface SearchService {
    void deleteFullText(UUID userToken, UUID fileToken);
    List<UUID> queryFileMetaData(UUID userToken, String query);
}
