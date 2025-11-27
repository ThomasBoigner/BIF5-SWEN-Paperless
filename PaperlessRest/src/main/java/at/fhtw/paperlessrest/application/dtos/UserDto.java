package at.fhtw.paperlessrest.application.dtos;

import at.fhtw.paperlessrest.domain.model.User;

import java.util.UUID;

public record UserDto(UUID token, String username) {
    public UserDto(User user) {
        this(user.getUserToken().token(), user.getUsername());
    }
}
