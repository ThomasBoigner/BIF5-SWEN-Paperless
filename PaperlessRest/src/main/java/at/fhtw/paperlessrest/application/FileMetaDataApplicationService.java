package at.fhtw.paperlessrest.application;

import at.fhtw.paperlessrest.application.commands.AddFullTextCommand;
import at.fhtw.paperlessrest.application.commands.UpdateFileCommand;
import at.fhtw.paperlessrest.application.commands.UploadFileCommand;
import at.fhtw.paperlessrest.application.dtos.FileMetaDataDto;
import at.fhtw.paperlessrest.domain.model.*;
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
    private final UserRepository userRepository;
    private final FileMetaDataEventPublisher fileMetaDataEventPublisher;
    private final FileService fileService;

    public List<FileMetaDataDto> getAllFileMetaData() {
        List<FileMetaDataDto> fileMetaDataList = fileMetaDataRepository.findAll().stream().map(FileMetaDataDto::new).toList();
        log.debug("Retrieved all ({}) file meta data", fileMetaDataList.size());
        return fileMetaDataList;
    }

    @Transactional(readOnly = false)
    public Optional<FileMetaDataDto> getFileMetaData(@Nullable UUID token) {
        Objects.requireNonNull(token, "token must not be null!");
        log.debug("Trying to retrieve file metadata with token {}", token);

        Optional<FileMetaDataDto> fileMetaData = fileMetaDataRepository.findFileMetaDataByFileToken(new FileToken(token)).map(FileMetaDataDto::new);

        fileMetaData.ifPresentOrElse(
                fmd -> log.debug("Found file meta data {} with token {}", fmd, token),
                () -> log.debug("No file meta data with id {} found", token));
        return fileMetaData;
    }

    @Transactional(readOnly = false)
    public FileMetaDataDto uploadFile(@Nullable UUID userToken, @Nullable MultipartFile file, @Nullable UploadFileCommand command) {
        Objects.requireNonNull(userToken, "userToken must not be null!");
        Objects.requireNonNull(file, "file must not be null!");
        Objects.requireNonNull(command, "command must not be null!");
        log.debug("Trying to create file with file {} and command {}", file.getOriginalFilename(), command);

        if (file.getContentType() != null && !file.getContentType().equals("application/pdf")) {
            throw new IllegalArgumentException("Invalid file content type! The file must be a pdf.");
        }

        Optional<User> entity = userRepository.findUserByUserToken(new UserToken(userToken));

        if (entity.isEmpty()) {
            log.warn("User with token {} can not be found!", userToken);
            throw new IllegalArgumentException(
                    "User with token %s can not be found!".formatted(userToken));
        }

        User user = entity.get();

        FileMetaData fileMetaData = user.uploadFile(
                file.getOriginalFilename(),
                file.getSize(),
                command.description()
        );

        fileService.uploadFile(fileMetaData.getFileToken().token(), file);
        userRepository.save(user);
        fileMetaDataEventPublisher.publishEvents(fileMetaData);
        log.info("Uploaded file {}", fileMetaData);
        return new FileMetaDataDto(fileMetaData);
    }

    @Transactional(readOnly = false)
    public void addFullText(@Nullable AddFullTextCommand command) {
        Objects.requireNonNull(command, "command must not be null!");
        log.debug("Trying to add full text with command {}", command);

        Optional<FileMetaData> entity = fileMetaDataRepository.findFileMetaDataByFileToken(new FileToken(command.fileToken()));

        if (entity.isEmpty()) {
            log.warn("File with token {} can not be found!", command.fileToken());
            return;
        }

        FileMetaData fileMetaData = entity.get();
        fileMetaData.addFullText(command.FullText());

        fileMetaDataEventPublisher.publishEvents(fileMetaData);
        fileMetaDataRepository.save(fileMetaData);
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
        fileMetaDataRepository.save(fileMetaData);
        fileMetaDataEventPublisher.publishEvents(fileMetaData);
        return new FileMetaDataDto(fileMetaData);
    }

    @Transactional(readOnly = false)
    public void deleteFileMetaData(@Nullable UUID token) {
        Objects.requireNonNull(token, "token must not be null!");
        fileService.deleteFile(token);
        fileMetaDataRepository.deleteByFileToken(new FileToken(token));
        log.info("Successfully deleted file with token {}", token);
    }
}
