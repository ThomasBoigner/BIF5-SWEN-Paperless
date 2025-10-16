package at.fhtw.paperlessrest.presentation;

import at.fhtw.paperlessrest.application.FileMetaDataApplicationService;
import at.fhtw.paperlessrest.application.commands.UploadFileCommand;
import at.fhtw.paperlessrest.application.dtos.FileMetaDataDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor

@Slf4j
@RestController
@RequestMapping(FileRestController.BASE_URL)
public class FileRestController {

    private final FileMetaDataApplicationService fileMetaDataApplicationService;

    public static final String BASE_URL = "/api/files";
    public static final String PATH_INDEX = "/";
    public static final String PATH_VAR_ID = "/{token}";
    public static final String ROUTE_ID = BASE_URL + PATH_VAR_ID;

    @GetMapping({"", PATH_INDEX})
    public HttpEntity<List<FileMetaDataDto>> getAllFileMetaData() {
        log.debug("Got Http GET request to retrieve all file meta data");
        List<FileMetaDataDto> fileMetaData = fileMetaDataApplicationService.getAllFileMetaData();
        return fileMetaData.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(fileMetaData);
    }

    @PostMapping(value = {"", PATH_INDEX})
    public HttpEntity<FileMetaDataDto> uploadFile(@RequestPart("file") MultipartFile file,
            @RequestPart("command") UploadFileCommand command) {
        log.debug("Got Http POST request to upload file with file {} and command {}", file.getOriginalFilename(), command);
        FileMetaDataDto fileMetaData = fileMetaDataApplicationService.uploadFile(file, command);
        return ResponseEntity.created(createSelfLink(fileMetaData)).body(fileMetaData);
    }

    private URI createSelfLink(FileMetaDataDto fileMetaData) {
        URI selfLink = UriComponentsBuilder.fromPath(ROUTE_ID)
                .uriVariables(Map.of("token", fileMetaData.fileToken()))
                .build().toUri();
        log.trace("Created self link {} for file {}", selfLink, fileMetaData);
        return selfLink;
    }
}
