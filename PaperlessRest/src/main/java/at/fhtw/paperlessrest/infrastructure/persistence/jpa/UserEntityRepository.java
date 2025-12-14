package at.fhtw.paperlessrest.infrastructure.persistence.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserEntityRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findUserEntityByUserToken(UUID userToken);
    void deleteUserEntityByUserToken(UUID userToken);
}
