package at.fhtw.paperlessrest.domain.model;

import lombok.Builder;
import lombok.Getter;
import org.jspecify.annotations.Nullable;
import org.springframework.util.Assert;

import java.util.*;

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

    public List<FileMetaData> getFiles() {
        return Collections.unmodifiableList(this.files);
    }

    public List<FileMetaData> getFilesWithFileTokens(List<FileToken> fileTokens) {
        return this.getFiles().stream().filter(fileMetaData -> fileTokens.contains(fileMetaData.getFileToken())).toList();
    }

    public Optional<FileMetaData> getFile(FileToken fileToken) {
        return this.getFiles().stream().filter(f -> f.getFileToken().equals(fileToken)).findFirst();
    }

    public boolean hasFile(FileToken fileToken) {
        return this.getFiles().stream().anyMatch(f -> f.getFileToken().equals(fileToken));
    }

    public FileMetaData uploadFile(@Nullable String fileName, long fileSize, @Nullable String description) {
        FileMetaData fileMetaData = FileMetaData.builder()
                .fileName(fileName)
                .fileSize(fileSize)
                .description(description)
                .build();

        this.files.add(fileMetaData);

        this.fileUploadedEvents.add(FileUploaded.builder()
                .userToken(this.userToken)
                .fileToken(fileMetaData.getFileToken())
                .fileName(fileMetaData.getFileName())
                .build());

        return fileMetaData;
    }

    public void addFullTextToFile(FileToken fileToken, String fullText) {
        Optional<FileMetaData> file = getFile(fileToken);

        if (file.isEmpty()) {
            return;
        }

        file.get().addFullText(fullText, this.userToken);
    }

    public FileMetaData updateFile(FileToken fileToken, @Nullable String description) {
        Optional<FileMetaData> fileOptional = getFile(fileToken);

        if (fileOptional.isEmpty()) {
            throw new IllegalArgumentException(
                    "File with token %s can not be found!".formatted(fileToken));
        }

        FileMetaData fileMetaData = fileOptional.get();
        fileMetaData.setDescription(description);
        return fileMetaData;
    }

    public void removeFile(FileToken fileToken) {
        this.files.removeIf(f -> f.getFileToken().equals(fileToken));
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
        if (!(o instanceof User that)) return false;
        return Objects.equals(userToken, that.userToken);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(userToken);
    }
}
