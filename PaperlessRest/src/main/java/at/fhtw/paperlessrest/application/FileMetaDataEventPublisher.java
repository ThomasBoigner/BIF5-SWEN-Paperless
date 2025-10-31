package at.fhtw.paperlessrest.application;

import at.fhtw.paperlessrest.domain.model.FileMetaData;

public interface FileMetaDataEventPublisher {
    public void publishEvents(FileMetaData fileMetaData);
}
