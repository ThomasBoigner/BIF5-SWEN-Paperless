package at.fhtw.batchprocessingworker.infrastructure.xml.jaxb;

import at.fhtw.batchprocessingworker.application.XmlService;
import at.fhtw.batchprocessingworker.domain.model.AccessRecord;
import at.fhtw.batchprocessingworker.infrastructure.xml.jaxb.model.AccessRecordXmlDocument;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor

@Slf4j
@Service
public class JaxbXmlService implements XmlService {

    private final Marshaller marshaller;

    @Override
    public void writeAccessRecordToFile(AccessRecord accessRecord) {
        log.debug("Trying to save accessRecord {} to xml file", accessRecord);
        AccessRecordXmlDocument accessRecordXmlDocument = new AccessRecordXmlDocument(accessRecord);

        try {
            File file = new File("./AccessRecord/%s.xml".formatted(accessRecordXmlDocument.getAccessRecordToken().toString()));

            File parentDir = file.getParentFile();
            if (parentDir != null && !parentDir.exists()) {
                parentDir.mkdirs();
            }

            marshaller.marshal(accessRecordXmlDocument, file);
        } catch (JAXBException e) {
            log.error("Got an error when trying to write a access record to the file system, message: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<AccessRecord> readAccessRecords() {
        return List.of();
    }

    @Override
    public void deleteAccessRecord(UUID accessRecordToken) {

    }
}
