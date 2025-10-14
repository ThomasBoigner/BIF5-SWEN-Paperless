package at.fhtw.paperlessrest.domain.model;

import jakarta.persistence.Embeddable;
import org.jspecify.annotations.Nullable;
import org.springframework.util.Assert;

import java.util.UUID;

@Embeddable
public record FileToken(@Nullable UUID fileToken) {
    public FileToken {
        Assert.notNull(fileToken, "fileToken must not be null!");
    }

    public FileToken() {
        this(UUID.randomUUID());
    }
}
