package at.fhtw.paperlessrest.application;

import at.fhtw.paperlessrest.application.commands.UploadFileCommand;
import at.fhtw.paperlessrest.application.dtos.FileMetaDataDto;
import at.fhtw.paperlessrest.domain.model.FileMetaData;
import at.fhtw.paperlessrest.domain.model.FileMetaDataRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor

@Service
@Slf4j
public class FileMetaDataApplicationService {
    private final FileMetaDataRepository fileMetaDataRepository;

    public List<FileMetaDataDto> getAllFileMetaData() {
        List<FileMetaDataDto> fileMetaDataList = fileMetaDataRepository.findAll().stream().map(FileMetaDataDto::new).toList();
        log.debug("Retrieved all ({}) file meta data", fileMetaDataList.size());
        return fileMetaDataList;
    }

    public FileMetaDataDto uploadFile(MultipartFile file, UploadFileCommand command) {
        Objects.requireNonNull(file);
        Objects.requireNonNull(command);

        FileMetaData fileMetaData = FileMetaData.builder()
                .fileName(file.getOriginalFilename())
                .fileSize(file.getSize())
                .description(command.description())
                .build();

        fileMetaDataRepository.save(fileMetaData);
        log.info("Uploaded file {}", fileMetaData);
        return new FileMetaDataDto(fileMetaData);
    }
}
