package at.fhtw.ocrworker.infrastructure.minio;

import at.fhtw.ocrworker.application.FileService;
import io.minio.*;
import io.minio.errors.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

@RequiredArgsConstructor

@Slf4j
@Service
public class MinioFileService implements FileService {
    private final MinioClient minioClient;

    @Override
    public InputStreamResource downloadFile(UUID token) {
        try {
            GetObjectResponse response = minioClient.getObject(
                GetObjectArgs.builder()
                        .bucket("files")
                        .object(token.toString())
                        .build()
            );

            return new InputStreamResource(response);
        } catch (ErrorResponseException ere) {
            log.warn("User tried to download a file that does not exist!");
            throw new IllegalArgumentException("File with the token %s does not exist!".formatted(token));
        } catch (InsufficientDataException | InternalException | InvalidKeyException |
                 InvalidResponseException | IOException | NoSuchAlgorithmException | ServerException |
                 XmlParserException e) {
            log.error("Got an error when trying to download a file to minio storage, message: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
