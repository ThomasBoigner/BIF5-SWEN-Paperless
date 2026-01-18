package at.fhtw.paperlessrest.infrastructure.persistence;

import at.fhtw.paperlessrest.domain.model.FileToken;
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
import java.util.UUID;

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
                        .userToken(user.getUserToken().token())
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
    public List<UUID> queryFileMetaData(UUID userToken, String query) {
        return fileDocumentRepository.search(userToken, query).stream().map(FileDocument::getFileToken).toList();
    }

    @Override
    public void removeFile(FileToken fileToken) {
        fileDocumentRepository.deleteById(fileToken.token());
    }

    @Override
    public void deleteUserByUserToken(UserToken token) {
        userEntityRepository.deleteUserEntityByUserToken(token.token());
        fileDocumentRepository.deleteAllByUserToken(token.token());
    }
}
