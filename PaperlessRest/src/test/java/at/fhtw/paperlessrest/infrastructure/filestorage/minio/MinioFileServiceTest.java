package at.fhtw.paperlessrest.infrastructure.filestorage.minio;

import io.minio.GetObjectResponse;
import io.minio.MinioClient;
import io.minio.errors.*;
import org.jspecify.annotations.NullUnmarked;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.InputStreamResource;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@NullUnmarked
@ExtendWith(MockitoExtension.class)
public class MinioFileServiceTest {
    private MinioFileService minioFileService;
    @Mock
    private MinioClient minioClient;

    @BeforeEach
    void setUp() {
        minioFileService = new MinioFileService(minioClient);
    }

    @Test
    void ensureUploadFileWorksProperly() throws Exception {
        // Given
        UUID token =  UUID.randomUUID();
        MultipartFile multipartFile = mock(MultipartFile.class);
        InputStream inputStream = mock(InputStream.class);

        when(minioClient.bucketExists(any())).thenReturn(true);
        when(multipartFile.getInputStream()).thenReturn(inputStream);
        when(multipartFile.getContentType()).thenReturn("application/pdf");

        // When
        minioFileService.uploadFile(token, multipartFile);

        // Then
        verify(minioClient).putObject(any());
    }

    @Test
    void ensureUploadCreatesBucketIfNotExists() throws Exception {
        // Given
        UUID token =  UUID.randomUUID();
        MultipartFile multipartFile = mock(MultipartFile.class);
        InputStream inputStream = mock(InputStream.class);

        when(minioClient.bucketExists(any())).thenReturn(false);
        when(multipartFile.getInputStream()).thenReturn(inputStream);
        when(multipartFile.getContentType()).thenReturn("application/pdf");

        // When
        minioFileService.uploadFile(token, multipartFile);

        // Then
        verify(minioClient).putObject(any());
        verify(minioClient).makeBucket(any());
    }

    @Test
    void ensureDownloadFileWorksProperly() throws Exception {
        // Given
        UUID token = UUID.randomUUID();
        GetObjectResponse response = mock(GetObjectResponse.class);

        when(minioClient.getObject(any())).thenReturn(response);

        // When
        InputStreamResource result = minioFileService.downloadFile(token);

        // Then
        verify(minioClient).getObject(any());
        assertThat(result).isNotNull();
        assertThat(response).isEqualTo(result.getInputStream());
    }
}
