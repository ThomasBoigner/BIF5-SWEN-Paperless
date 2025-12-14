package at.fhtw.paperlessrest.domain.model;

import java.util.Optional;

public interface UserRepository {
    User save(User user);
    Optional<User> findUserByUserToken(UserToken token);
    void deleteUserByUserToken(UserToken token);
}
