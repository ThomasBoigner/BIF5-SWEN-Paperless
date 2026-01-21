package at.fhtw.batchprocessingworker.application;

import at.fhtw.batchprocessingworker.application.commands.CreateAccessRecordCommand;
import at.fhtw.batchprocessingworker.domain.model.AccessRecord;
import at.fhtw.batchprocessingworker.domain.model.AccessRecordRead;
import at.fhtw.batchprocessingworker.domain.model.FileToken;
import at.fhtw.batchprocessingworker.domain.model.UserToken;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor

@Service
@Slf4j
public class AccessRecordApplicationService {
    private final XmlService xmlService;
    private final AccessRecordReadEventPublisher accessRecordReadEventPublisher;

    public void createAccessRecord(CreateAccessRecordCommand command) {
        Objects.requireNonNull(command, "command must not be null!");
        log.debug("Trying to create access record with command {}", command);

        AccessRecord accessRecord = AccessRecord.builder()
                .userToken(new UserToken(command.userToken()))
                .fileToken(new FileToken(command.fileToken()))
                .build();

        xmlService.writeAccessRecordToFile(accessRecord);
    }

    @Scheduled(fixedDelay = 60000)
    public void readAccessRecords() {
        log.debug("Trying to read access records");

        List<AccessRecord> accessRecords = xmlService.readAccessRecords();

        accessRecords.forEach(accessRecord ->
                xmlService.deleteAccessRecord(accessRecord.getAccessRecordToken().token())
        );
        accessRecords.stream()
                .map(accessRecord -> AccessRecordRead.builder()
                        .userToken(accessRecord.getUserToken())
                        .fileToken(accessRecord.getFileToken())
                        .numberOfAccesses(accessRecord.getNumberOfAccesses())
                .build())
                .forEach(accessRecordReadEventPublisher::publishEvents);
    }
}
