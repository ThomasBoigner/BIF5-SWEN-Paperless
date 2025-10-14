package at.fhtw.paperlessrest.domain.model;

import jakarta.persistence.Embeddable;
import lombok.Builder;
import org.jspecify.annotations.Nullable;
import org.springframework.util.Assert;

import java.util.Objects;

@Embeddable
public record FileName(@Nullable String name, @Nullable String fileExtension) {
    @Builder
    public FileName {
        Objects.requireNonNull(name, "name must not be null!");
        Objects.requireNonNull(fileExtension, "fileExtension must not be null!");
        Assert.isTrue(!name.isBlank(), "Name must not be blank!");
        Assert.isTrue(!fileExtension.isBlank(), "Name must not be blank!");
    }
}
