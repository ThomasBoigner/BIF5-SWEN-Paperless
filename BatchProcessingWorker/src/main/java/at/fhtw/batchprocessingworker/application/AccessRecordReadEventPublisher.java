package at.fhtw.batchprocessingworker.application;

import at.fhtw.batchprocessingworker.domain.model.AccessRecordRead;

public interface AccessRecordReadEventPublisher {
    void publishEvents(AccessRecordRead event);
}
