package at.fhtw.batchprocessingworker.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.jspecify.annotations.Nullable;
import org.springframework.util.Assert;

import java.util.Objects;

@Getter
@AllArgsConstructor
@ToString
public class AccessRecord {
    private AccessRecordToken accessRecordToken;
    private UserToken userToken;
    private FileToken fileToken;
    private int numberOfAccesses;

    @Builder
    public AccessRecord(@Nullable UserToken userToken, @Nullable FileToken fileToken) {
        this.setAccessRecordToken(new AccessRecordToken());
        this.setUserToken(userToken);
        this.setFileToken(fileToken);
        this.setNumberOfAccesses((int) ((Math.random() * (10 - 1)) + 1));
    }

    private void setAccessRecordToken(@Nullable AccessRecordToken accessRecordToken) {
        Objects.requireNonNull(accessRecordToken, "Token must not be null!");
        this.accessRecordToken = accessRecordToken;
    }

    private void setUserToken(@Nullable UserToken userToken) {
        Objects.requireNonNull(userToken, "User token must not be null!");
        this.userToken = userToken;
    }

    private void setFileToken(@Nullable FileToken fileToken) {
        Objects.requireNonNull(fileToken, "File token must not be null!");
        this.fileToken = fileToken;
    }

    private void setNumberOfAccesses(int numberOfAccesses) {
        Assert.isTrue(numberOfAccesses >= 0, "Number of accesses can not be smaller than 1!");
        this.numberOfAccesses = numberOfAccesses;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        AccessRecord that = (AccessRecord) o;
        return Objects.equals(accessRecordToken, that.accessRecordToken);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(accessRecordToken);
    }
}
