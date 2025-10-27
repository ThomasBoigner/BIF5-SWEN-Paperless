package at.fhtw.paperlessrest.presentation;

import at.fhtw.paperlessrest.application.FileMetaDataApplicationService;
import at.fhtw.paperlessrest.application.FileService;
import at.fhtw.paperlessrest.application.commands.UpdateFileCommand;
import at.fhtw.paperlessrest.application.commands.UploadFileCommand;
import at.fhtw.paperlessrest.application.dtos.FileMetaDataDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.Nullable;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RequiredArgsConstructor

@Slf4j
@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping(FileRestController.BASE_URL)
public class FileRestController {

    private final FileMetaDataApplicationService fileMetaDataApplicationService;
    private final FileService fileService;

    public static final String BASE_URL = "/api/files";
    public static final String PATH_INDEX = "/";
    public static final String PATH_VAR_ID = "/{token}";
    public static final String ROUTE_ID = BASE_URL + PATH_VAR_ID;
    public static final String ROUTE_DOWNLOAD = PATH_VAR_ID + "/download";

    @GetMapping({"", PATH_INDEX})
    public HttpEntity<List<FileMetaDataDto>> getAllFileMetaData() {
        log.debug("Got Http GET request to retrieve all file meta data");
        List<FileMetaDataDto> fileMetaData = fileMetaDataApplicationService.getAllFileMetaData();
        return fileMetaData.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(fileMetaData);
    }

    @GetMapping({PATH_VAR_ID})
    public HttpEntity<FileMetaDataDto> getFileMetaData(@PathVariable UUID token) {
        log.debug("Got Http GET request to retrieve specific file with token {} ", token);
        return fileMetaDataApplicationService.getFileMetaData(token)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping(value = {ROUTE_DOWNLOAD}, produces = MediaType.APPLICATION_PDF_VALUE)
    public HttpEntity<InputStreamResource> getFileContent(@PathVariable UUID token) {
        log.debug("Got Http GET request to retrieve file content of file with token {} ", token);
        return ResponseEntity.ok(fileService.downloadFile(token));
    }

    @PostMapping(value = {"", PATH_INDEX})
    public HttpEntity<FileMetaDataDto> uploadFile(@Nullable @RequestPart("file") MultipartFile file,
           @Nullable @RequestPart("command") UploadFileCommand command) {
        log.debug("Got Http POST request to upload file with file {} and command {}", file != null ? file.getOriginalFilename() : "[name not found]", command);
        FileMetaDataDto fileMetaData = fileMetaDataApplicationService.uploadFile(file, command);
        return ResponseEntity.created(createSelfLink(fileMetaData)).body(fileMetaData);
    }

    @PutMapping(PATH_VAR_ID)
    public HttpEntity<FileMetaDataDto> updateFileMetaData(@PathVariable UUID token, @RequestBody UpdateFileCommand command) {
        log.debug("Got Http PUT request for token {} with file update command {}", token, command);
        return ResponseEntity.ok(fileMetaDataApplicationService.updateFileMetaData(token, command));
    }

    @DeleteMapping(PATH_VAR_ID)
    public HttpEntity<FileMetaDataDto> deleteFileMetaData(@PathVariable UUID token) {
        log.debug("Got Http DELETE request to delete file with token {}", token);
        fileMetaDataApplicationService.deleteFileMetaData(token);
        return ResponseEntity.ok().build();
    }

    private URI createSelfLink(FileMetaDataDto fileMetaData) {
        URI selfLink = UriComponentsBuilder.fromPath(ROUTE_ID)
                .uriVariables(Map.of("token", fileMetaData.fileToken()))
                .build().toUri();
        log.trace("Created self link {} for file {}", selfLink, fileMetaData);
        return selfLink;
    }
}
