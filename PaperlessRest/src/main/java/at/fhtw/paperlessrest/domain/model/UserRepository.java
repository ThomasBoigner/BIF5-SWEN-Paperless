package at.fhtw.paperlessrest.domain.model;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository {
    User save(User user);
    Optional<User> findUserByUserToken(UserToken token);
    List<UUID> queryFileMetaData(UUID userToken, String query);
    void removeFile(FileToken fileToken);
    void deleteUserByUserToken(UserToken token);
}
