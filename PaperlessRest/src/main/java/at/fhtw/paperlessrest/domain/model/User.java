package at.fhtw.paperlessrest.domain.model;

import lombok.Builder;
import lombok.Getter;
import org.jspecify.annotations.Nullable;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Getter
public class User {
    @Nullable
    private Long id;
    private UserToken userToken;
    private String username;
    private final List<FileMetaData> files;

    private final List<FileUploaded> fileUploadedEvents;

    @Builder
    public User(@Nullable UserToken userToken, @Nullable String username) {
        this.setUserToken(userToken);
        this.setUsername(username);
        this.files = new ArrayList<>();

        this.fileUploadedEvents = new ArrayList<>();
    }

    public User(@Nullable Long id, UserToken userToken, String username, List<FileMetaData> files) {
        this.id = id;
        this.userToken = userToken;
        this.username = username;
        this.files = files;

        this.fileUploadedEvents = new ArrayList<>();
    }

    public FileMetaData uploadFile(@Nullable String fileName, long fileSize, @Nullable String description) {
        FileMetaData fileMetaData = FileMetaData.builder()
                .fileName(fileName)
                .fileSize(fileSize)
                .description(description)
                .build();

        this.files.add(fileMetaData);

        this.fileUploadedEvents.add(FileUploaded.builder()
                .fileToken(fileMetaData.getFileToken())
                .build());

        return fileMetaData;
    }

    public List<FileMetaData> getFiles() {
        return Collections.unmodifiableList(this.files);
    }

    private void setUserToken(@Nullable UserToken userToken) {
        Objects.requireNonNull(userToken, "userToken must not be null!");
        this.userToken = userToken;
    }

    private void setUsername(@Nullable String username) {
        Objects.requireNonNull(username, "username must not be null!");
        Assert.isTrue(!username.isBlank(), "username must not be blank!");
        this.username = username;
    }

    @Override
    public String toString() {
        return "User{" +
                "userToken=" + userToken +
                ", username='" + username + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(userToken, user.userToken);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(userToken);
    }
}
