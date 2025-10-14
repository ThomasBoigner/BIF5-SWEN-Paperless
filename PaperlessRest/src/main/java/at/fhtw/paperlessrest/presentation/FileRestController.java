package at.fhtw.paperlessrest.presentation;

import at.fhtw.paperlessrest.application.FileMetaDataApplicationService;
import at.fhtw.paperlessrest.application.dtos.FileMetaDataDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor

@Slf4j
@RestController
@RequestMapping(FileRestController.BASE_URL)
public class FileRestController {

    private final FileMetaDataApplicationService fileMetaDataApplicationService;

    public static final String BASE_URL = "/api/files";
    public static final String PATH_INDEX = "/";

    @GetMapping({"", PATH_INDEX})
    public HttpEntity<List<FileMetaDataDto>> getAllFileMetaData() {
        log.debug("Got Http GET request to retrieve all file meta data");
        List<FileMetaDataDto> fileMetaData = fileMetaDataApplicationService.getAllFileMetaData();
        return (fileMetaData.isEmpty())
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(fileMetaData);
    }
}
