package at.fhtw.paperlessrest.application;

import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public interface FileService {
    void uploadFile(UUID token, MultipartFile file);
}
