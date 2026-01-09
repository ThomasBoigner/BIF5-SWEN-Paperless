package at.fhtw.paperlessrest.infrastructure.persistence;

import at.fhtw.paperlessrest.domain.model.User;
import at.fhtw.paperlessrest.domain.model.UserRepository;
import at.fhtw.paperlessrest.domain.model.UserToken;
import at.fhtw.paperlessrest.infrastructure.persistence.elasticsearch.FileDocument;
import at.fhtw.paperlessrest.infrastructure.persistence.elasticsearch.FileDocumentRepository;
import at.fhtw.paperlessrest.infrastructure.persistence.jpa.UserEntity;
import at.fhtw.paperlessrest.infrastructure.persistence.jpa.UserEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor

@Repository
public class JpaElasticsearchUserRepository implements UserRepository {
    private final UserEntityRepository userEntityRepository;
    private final FileDocumentRepository fileDocumentRepository;

    @Override
    public User save(User user) {
        UserEntity userEntity = new UserEntity(user);
        this.userEntityRepository.save(userEntity);
        user.getFiles().forEach(file -> fileDocumentRepository.save(FileDocument.builder()
                        .fileToken(file.getFileToken().token())
                        .fileName(file.getFileName())
                        .fullText(file.getFullText())
                        .summary(file.getSummary())
                .build())
        );
        return user;
    }

    @Override
    public Optional<User> findUserByUserToken(UserToken token) {
        Optional<UserEntity> user = userEntityRepository.findUserEntityByUserToken(token.token());

        if (user.isEmpty()) {
            return Optional.empty();
        }

        List<FileDocument> fileDocuments = user.get().getFiles().stream().map(fileMetaDataEntity -> fileDocumentRepository.findById(fileMetaDataEntity.getFileToken())).filter(Optional::isPresent).map(Optional::get).toList();
        return user.map(userEntity -> userEntity.toUser(fileDocuments));
    }

    @Override
    public void deleteUserByUserToken(UserToken token) {
        userEntityRepository.deleteUserEntityByUserToken(token.token());
    }
}
