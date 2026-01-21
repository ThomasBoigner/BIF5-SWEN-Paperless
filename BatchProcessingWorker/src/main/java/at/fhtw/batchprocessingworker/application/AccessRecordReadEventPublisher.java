package at.fhtw.batchprocessingworker.application;

import at.fhtw.batchprocessingworker.domain.model.AccessRecordRead;

import java.util.List;

public interface AccessRecordReadEventPublisher {
    void publishEvents(AccessRecordRead event);
}
