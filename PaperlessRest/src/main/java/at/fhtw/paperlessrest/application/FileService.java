package at.fhtw.paperlessrest.application;

import org.springframework.core.io.InputStreamResource;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public interface FileService {
    void uploadFile(UUID token, MultipartFile file);
    InputStreamResource downloadFile(UUID token);
    void deleteFile(UUID token);
}
