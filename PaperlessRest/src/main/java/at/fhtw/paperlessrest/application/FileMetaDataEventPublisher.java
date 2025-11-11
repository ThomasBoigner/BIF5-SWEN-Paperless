package at.fhtw.paperlessrest.application;

import at.fhtw.paperlessrest.domain.model.FileMetaData;

public interface FileMetaDataEventPublisher {
    void publishEvents(FileMetaData fileMetaData);
}
