package at.fhtw.batchprocessingworker.infrastructure.xml.jaxb.adapter;

import jakarta.xml.bind.annotation.adapters.XmlAdapter;

import java.util.UUID;

public class UUIDAdapter extends XmlAdapter<String, UUID> {
    @Override
    public UUID unmarshal(String s) {
        return UUID.fromString(s);
    }

    @Override
    public String marshal(UUID uuid) {
        return uuid.toString();
    }
}
