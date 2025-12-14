package at.fhtw.paperlessrest.infrastructure.persistence.jpa;

import at.fhtw.paperlessrest.domain.model.User;
import at.fhtw.paperlessrest.domain.model.UserRepository;
import at.fhtw.paperlessrest.domain.model.UserToken;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@RequiredArgsConstructor

@Repository
public class JpaUserRepository implements UserRepository {
    private final UserEntityRepository userEntityRepository;

    @Override
    public User save(User user) {
        UserEntity userEntity = new UserEntity(user);
        this.userEntityRepository.save(userEntity);
        return user;
    }

    @Override
    public Optional<User> findUserByUserToken(UserToken token) {
        return userEntityRepository.findUserEntityByUserToken(token.token()).map(UserEntity::toUser);
    }

    @Override
    public void deleteUserByUserToken(UserToken token) {
        userEntityRepository.deleteUserEntityByUserToken(token.token());
    }
}
