package at.fhtw.paperlessrest.domain.model;

import java.util.UUID;

public record FileToken(UUID token) {
    public FileToken() {
        this(UUID.randomUUID());
    }
}
