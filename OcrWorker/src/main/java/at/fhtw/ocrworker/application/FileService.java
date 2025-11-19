package at.fhtw.ocrworker.application;

import org.springframework.core.io.InputStreamResource;

import java.util.UUID;

public interface FileService {
    InputStreamResource downloadFile(UUID token);
}
