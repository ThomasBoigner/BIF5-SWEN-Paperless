package at.fhtw.batchprocessingworker.infrastructure.xml.jaxb;

import at.fhtw.batchprocessingworker.infrastructure.xml.jaxb.model.AccessRecordXmlDocument;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JaxbConfiguration {
    @Bean
    public JAXBContext context() throws JAXBException {
        return JAXBContext.newInstance(AccessRecordXmlDocument.class);
    }

    @Bean
    public Marshaller marshaller(JAXBContext context) throws JAXBException {
        return context.createMarshaller();
    }

    @Bean
    public Unmarshaller unmarshaller(JAXBContext context) throws JAXBException {
        return context.createUnmarshaller();
    }
}
