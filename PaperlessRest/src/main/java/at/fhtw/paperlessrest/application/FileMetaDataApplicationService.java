package at.fhtw.paperlessrest.application;

import at.fhtw.paperlessrest.application.commands.DeleteFileCommand;
import at.fhtw.paperlessrest.application.commands.UpdateFileCommand;
import at.fhtw.paperlessrest.application.commands.UploadFileCommand;
import at.fhtw.paperlessrest.application.dtos.FileMetaDataDto;
import at.fhtw.paperlessrest.domain.model.FileMetaData;
import at.fhtw.paperlessrest.domain.model.FileMetaDataRepository;
import at.fhtw.paperlessrest.domain.model.FileToken;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor

@Service
@Slf4j
@Transactional(readOnly = true)
public class FileMetaDataApplicationService {
    private final FileMetaDataRepository fileMetaDataRepository;

    public List<FileMetaDataDto> getAllFileMetaData() {
        List<FileMetaDataDto> fileMetaDataList = fileMetaDataRepository.findAll().stream().map(FileMetaDataDto::new).toList();
        log.debug("Retrieved all ({}) file meta data", fileMetaDataList.size());
        return fileMetaDataList;
    }

    @Transactional(readOnly = false)
    public FileMetaDataDto getFileMetaData(@Nullable UUID token) {
        Objects.requireNonNull(token, "token must not be null!");
        log.debug("Retrieving file metadata for token {}", token);

        Optional<FileMetaData> entity = fileMetaDataRepository.findFileMetaDataByFileToken(new FileToken(token));

        if (entity.isEmpty()) {
            log.warn("File with token {} cannot be found!", token);
            throw new IllegalArgumentException(
                    "File with token %s cannot be found!".formatted(token));
        }

        FileMetaDataDto fileMetaDataDto = new FileMetaDataDto(entity.get());
        log.info("Successfully retrieved file metadata: {}", fileMetaDataDto);
        return fileMetaDataDto;
    }

    @Transactional(readOnly = false)
    public FileMetaDataDto uploadFile(@Nullable MultipartFile file, @Nullable UploadFileCommand command) {
        Objects.requireNonNull(file, "file must not be null!");
        Objects.requireNonNull(command, "command must not be null!");
        log.debug("Trying to create file with file {} and command {}", file.getOriginalFilename(), command);

        FileMetaData fileMetaData = FileMetaData.builder()
                .fileName(file.getOriginalFilename())
                .fileSize(file.getSize())
                .description(command.description())
                .build();

        fileMetaDataRepository.save(fileMetaData);
        log.info("Uploaded file {}", fileMetaData);
        return new FileMetaDataDto(fileMetaData);
    }

    @Transactional(readOnly = false)
    public FileMetaDataDto updateFileMetaData(@Nullable UUID token, @Nullable UpdateFileCommand command) {
        Objects.requireNonNull(token, "token must not be null!");
        Objects.requireNonNull(command, "command must not be null!");

        Optional<FileMetaData> entity = fileMetaDataRepository.findFileMetaDataByFileToken(new FileToken(token));

        if (entity.isEmpty()) {
            log.warn("File with token {} can not be found!", token);
            throw new IllegalArgumentException(
                    "File with token %s can not be found!".formatted(token));
        }

        FileMetaData fileMetaData = entity.get();

        fileMetaData.setDescription(command.description());

        log.info("Successfully updated file meta data {}", fileMetaData);
        return new FileMetaDataDto(fileMetaDataRepository.save(fileMetaData));
    }

    @Transactional(readOnly = false)
    public void deleteFileMetaData(@Nullable UUID token, @Nullable DeleteFileCommand command) {
        Objects.requireNonNull(token, "token must not be null!");
        Objects.requireNonNull(command, "command must not be null!");

        Optional<FileMetaData> entity = fileMetaDataRepository.findFileMetaDataByFileToken(new FileToken(token));

        if (entity.isEmpty()) {
            log.warn("File with token {} can not be found!", token);
            throw new IllegalArgumentException(
                    "File with token %s can not be found!".formatted(token));
        }

        log.info("Successfully updated file meta data {}", entity);
        fileMetaDataRepository.delete(entity.get());
    }
}
