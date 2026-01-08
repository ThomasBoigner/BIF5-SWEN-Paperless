package at.fhtw.genaiworker.application;

import at.fhtw.genaiworker.domain.model.SummaryCreated;

public interface SummaryCreatedEventPublisher {
    void publishEvent(SummaryCreated event);
}
