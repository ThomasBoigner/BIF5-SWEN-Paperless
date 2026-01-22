package at.fhtw.batchprocessingworker.infrastructure.xml.jaxb;

import at.fhtw.batchprocessingworker.application.XmlService;
import at.fhtw.batchprocessingworker.domain.model.AccessRecord;
import at.fhtw.batchprocessingworker.infrastructure.xml.jaxb.model.AccessRecordXmlDocument;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

@RequiredArgsConstructor

@Slf4j
@Service
public class JaxbXmlService implements XmlService {
    private final Marshaller marshaller;
    private final Unmarshaller unmarshaller;

    @Override
    public void writeAccessRecordToFile(AccessRecord accessRecord) {
        log.debug("Trying to save accessRecord {} to xml file", accessRecord);
        AccessRecordXmlDocument accessRecordXmlDocument = new AccessRecordXmlDocument(accessRecord);

        try {
            File dir = new File("./AccessRecord");

            if (!dir.exists()) {
                dir.mkdirs();
            }

            marshaller.marshal(accessRecordXmlDocument, new File("./AccessRecord/%s.xml".formatted(accessRecordXmlDocument.getAccessRecordToken())));
        } catch (JAXBException e) {
            log.error("Got an error when trying to write a access record to the file system, message: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<AccessRecord> readAccessRecords() {
        log.debug("Trying to read all xml files");
        File dir = new File("./AccessRecord");
        File[] files = dir.listFiles();

        if (files == null || files.length == 0) {
            log.warn("Directory does not exist or is empty");
            return List.of();
        }

        return Stream.of(files)
                .filter(file -> !file.isDirectory())
                .map(file -> {
                    try {
                        return unmarshaller.unmarshal(file);
                    } catch (JAXBException e) {
                        log.error("Got an error when trying to read a access record from the file system, message: {}", e.getMessage());
                        throw new RuntimeException(e);
                    }
                })
                .map(accessRecordXmlDocument -> ((AccessRecordXmlDocument) accessRecordXmlDocument).toAccessRecord())
                .toList();
    }

    @Override
    public void deleteAccessRecord(UUID accessRecordToken) {
        log.debug("Trying to delete xml document with name {}.xml", accessRecordToken);
        new File("./AccessRecord/%s.xml".formatted(accessRecordToken)).delete();
    }
}
