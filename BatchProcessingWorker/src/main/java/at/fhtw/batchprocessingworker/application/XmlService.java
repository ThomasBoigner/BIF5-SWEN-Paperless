package at.fhtw.batchprocessingworker.application;

import at.fhtw.batchprocessingworker.domain.model.AccessRecord;

import java.util.List;
import java.util.UUID;

public interface XmlService {
    void writeAccessRecordToFile(AccessRecord accessRecord);
    List<AccessRecord> readAccessRecords();
    void deleteAccessRecord(UUID accessRecordToken);

}
