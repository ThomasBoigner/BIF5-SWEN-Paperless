package at.fhtw.paperlessrest.infrastructure.persistence.jpa;

import at.fhtw.paperlessrest.domain.model.User;
import at.fhtw.paperlessrest.domain.model.UserToken;
import jakarta.persistence.*;
import lombok.Getter;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Nullable
    private Long id;
    private UUID userToken;
    private String username;
    @OneToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REMOVE})
    private List<FileMetaDataEntity> files;

    public UserEntity() {
        this.userToken = UUID.randomUUID();
        this.username = "";
        this.files = new ArrayList<>();
    }

    public UserEntity(User user) {
        this.id = user.getId();
        this.userToken = user.getUserToken().token();
        this.username = user.getUsername();
        this.files = user.getFiles().stream().map(FileMetaDataEntity::new).toList();
    }

    public User toUser() {
        return new User(
                this.id,
                new UserToken(this.userToken),
                this.username,
                this.files.stream().map(FileMetaDataEntity::toFileMetaData).toList()
        );
    }
}
