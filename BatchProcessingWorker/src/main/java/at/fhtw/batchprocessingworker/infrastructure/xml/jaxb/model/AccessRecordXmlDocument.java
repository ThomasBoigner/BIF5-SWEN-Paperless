package at.fhtw.batchprocessingworker.infrastructure.xml.jaxb.model;

import at.fhtw.batchprocessingworker.domain.model.AccessRecord;
import at.fhtw.batchprocessingworker.domain.model.AccessRecordToken;
import at.fhtw.batchprocessingworker.domain.model.FileToken;
import at.fhtw.batchprocessingworker.domain.model.UserToken;
import at.fhtw.batchprocessingworker.infrastructure.xml.jaxb.adapter.UUIDAdapter;
import jakarta.xml.bind.annotation.*;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@XmlRootElement(name = "AccessRecord")
@XmlType(propOrder = { "accessRecordToken", "userToken", "fileToken", "numberOfAccesses" })
@XmlAccessorType(XmlAccessType.FIELD)
public class AccessRecordXmlDocument {
    @XmlAttribute
    @XmlJavaTypeAdapter(UUIDAdapter.class)
    private UUID accessRecordToken;
    @XmlElement
    @XmlJavaTypeAdapter(UUIDAdapter.class)
    private UUID userToken;
    @XmlElement
    @XmlJavaTypeAdapter(UUIDAdapter.class)
    private UUID fileToken;
    @XmlElement(name = "numberOfAccesses")
    private int numberOfAccesses;

    public AccessRecordXmlDocument() {
        this.accessRecordToken = UUID.randomUUID();
        this.userToken = UUID.randomUUID();
        this.fileToken = UUID.randomUUID();
        this.numberOfAccesses = 1;
    }

    public AccessRecordXmlDocument(AccessRecord accessRecord) {
        this.accessRecordToken = accessRecord.getAccessRecordToken().token();
        this.userToken = accessRecord.getUserToken().token();
        this.fileToken = accessRecord.getFileToken().token();
        this.numberOfAccesses = accessRecord.getNumberOfAccesses();
    }

    public AccessRecord toAccessRecord() {
        return new AccessRecord(
                new AccessRecordToken(this.accessRecordToken),
                new UserToken(this.userToken),
                new FileToken(this.fileToken),
                numberOfAccesses
        );
    }
}
