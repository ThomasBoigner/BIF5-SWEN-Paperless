package at.fhtw.paperlessrest.application;

import at.fhtw.paperlessrest.domain.model.User;

public interface UserEventPublisher {
    void publishEvents(User user);
}
