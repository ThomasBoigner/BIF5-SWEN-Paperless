package at.fhtw.paperlessrest.domain.model;

import jakarta.persistence.Embeddable;

import java.util.UUID;

@Embeddable
public record FileToken(UUID token) {
    public FileToken() {
        this(UUID.randomUUID());
    }
}
