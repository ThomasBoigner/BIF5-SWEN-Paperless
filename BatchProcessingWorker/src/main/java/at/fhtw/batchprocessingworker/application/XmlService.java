package at.fhtw.batchprocessingworker.application;

import at.fhtw.batchprocessingworker.domain.model.AccessRecord;

public interface XmlService {
    void writeAccessRecordToFile(AccessRecord accessRecord);

}
