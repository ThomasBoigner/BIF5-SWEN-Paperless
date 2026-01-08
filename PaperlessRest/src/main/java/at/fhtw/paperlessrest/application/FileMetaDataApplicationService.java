package at.fhtw.paperlessrest.application;

import at.fhtw.paperlessrest.application.commands.AddFullTextCommand;
import at.fhtw.paperlessrest.application.commands.UpdateFileCommand;
import at.fhtw.paperlessrest.application.commands.UploadFileCommand;
import at.fhtw.paperlessrest.application.dtos.FileMetaDataDto;
import at.fhtw.paperlessrest.domain.model.*;
import at.fhtw.paperlessrest.infrastructure.persistence.jpa.UserEntityRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.Nullable;
import org.springframework.core.io.InputStreamResource;
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
    private final UserRepository userRepository;
    private final UserEventPublisher userEventPublisher;
    private final FileService fileService;
    private final SearchService searchService;
    private final UserEntityRepository userEntityRepository;

    public List<FileMetaDataDto> getAllFileMetaData(@Nullable UUID userToken) {
        Objects.requireNonNull(userToken, "userToken must not be null!");
        log.debug("Trying to get all file of user with token {}", userToken);

        Optional<User> entity = userRepository.findUserByUserToken(new UserToken(userToken));

        if (entity.isEmpty()) {
            return List.of();
        }

        User user = entity.get();

        List<FileMetaDataDto> fileMetaDataList = user.getFiles().stream().map(FileMetaDataDto::new).toList();
        log.debug("Retrieved all ({}) file meta data", fileMetaDataList.size());
        return fileMetaDataList;
    }

    public List<FileMetaDataDto> queryFileMetaDta(@Nullable UUID userToken, String query) {
        Objects.requireNonNull(userToken, "userToken must not be null!");
        log.debug("Trying to get all file of user with token {} with query {}", userToken, query);

        List<UUID> fileTokens = searchService.queryFileMetaData(userToken, query);

        Optional<User> entity = userRepository.findUserByUserToken(new UserToken(userToken));

        if (entity.isEmpty()) {
            return List.of();
        }

        User user = entity.get();

        List<FileMetaDataDto> fileMetaDataList = user.getFilesWithFileTokens(fileTokens.stream().map(FileToken::new).toList())
                .stream().map(FileMetaDataDto::new).toList();
        log.debug("Retrieved all ({}) file meta data that matches query {}", fileMetaDataList.size(), query);
        return fileMetaDataList;
    }

    public Optional<FileMetaDataDto> getFileMetaData(@Nullable UUID userToken, @Nullable UUID fileToken) {
        Objects.requireNonNull(fileToken, "fileToken must not be null!");
        Objects.requireNonNull(userToken, "userToken must not be null!");
        log.debug("Trying to retrieve file metadata with token {} of user with token {}", fileToken, userToken);

        Optional<User> entity = userRepository.findUserByUserToken(new UserToken(userToken));

        if (entity.isEmpty()) {
            return Optional.empty();
        }

        User user = entity.get();

        Optional<FileMetaDataDto> fileMetaData = user.getFile(new FileToken(fileToken)).map(FileMetaDataDto::new);

        fileMetaData.ifPresentOrElse(
                fmd -> log.debug("Found file meta data {} with token {}", fmd, fileToken),
                () -> log.debug("No file meta data with id {} found", fileToken));
        return fileMetaData;
    }

    public Optional<InputStreamResource> downloadFile(@Nullable UUID userToken, @Nullable UUID fileToken) {
        Objects.requireNonNull(fileToken, "fileToken must not be null!");
        Objects.requireNonNull(userToken, "userToken must not be null!");
        log.debug("Trying to retrieve file with token {} of user with token {}", fileToken, userToken);

        Optional<User> entity = userRepository.findUserByUserToken(new UserToken(userToken));

        if (entity.isEmpty()) {
            return Optional.empty();
        }

        User user = entity.get();

        return user.hasFile(new FileToken(fileToken))
                ? Optional.of(fileService.downloadFile(fileToken))
                : Optional.empty();
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
        userEventPublisher.publishEvents(user);
        log.info("Uploaded file {}", fileMetaData);
        return new FileMetaDataDto(fileMetaData);
    }

    @Transactional(readOnly = false)
    public void addFullText(@Nullable AddFullTextCommand command) {
        Objects.requireNonNull(command, "command must not be null!");
        log.debug("Trying to add full text with command {}", command);

        Optional<User> entity = userRepository.findUserByUserToken(new UserToken(command.userToken()));

        if (entity.isEmpty()) {
            log.warn("User with token {} can not be found!", command.fileToken());
            return;
        }

        User user = entity.get();

        user.addFullTextToFile(new FileToken(command.fileToken()), command.fullText());

        userRepository.save(user);
        userEventPublisher.publishEvents(user);
        log.info("Full text added to file {}", command.fileToken());
    }

    @Transactional(readOnly = false)
    public FileMetaDataDto updateFileMetaData(@Nullable UUID userToken, @Nullable UUID fileToken, @Nullable UpdateFileCommand command) {
        Objects.requireNonNull(userToken, "userToken must not be null!");
        Objects.requireNonNull(fileToken, "fileToken must not be null!");
        Objects.requireNonNull(command, "command must not be null!");
        log.debug("Trying to update file metadata with fileToken {} of user with userToken {}", fileToken, userToken);

        Optional<User> entity = userRepository.findUserByUserToken(new UserToken(userToken));

        if (entity.isEmpty()) {
            log.warn("User with token {} can not be found!", userToken);
            throw new IllegalArgumentException(
                    "User with token %s can not be found!".formatted(userToken));
        }

        User user = entity.get();

        FileMetaData fileMetaData = user.updateFile(new FileToken(fileToken), command.description());

        log.info("Successfully updated file meta data {}", fileMetaData);
        userRepository.save(user);
        userEventPublisher.publishEvents(user);
        return new FileMetaDataDto(fileMetaData);
    }

    @Transactional(readOnly = false)
    public void deleteFileMetaData(@Nullable UUID userToken, @Nullable UUID fileToken) {
        Objects.requireNonNull(fileToken, "fileToken must not be null!");
        Objects.requireNonNull(userToken, "userToken must not be null!");
        log.debug("Trying to delete file meta data with fileToken {}", fileToken);

        Optional<User> entity = userRepository.findUserByUserToken(new UserToken(userToken));

        if (entity.isEmpty()) {
            return;
        }

        User user = entity.get();

        fileService.deleteFile(fileToken);
        searchService.deleteFullText(userToken, fileToken);
        user.removeFile(new FileToken(fileToken));
        userRepository.save(user);
        log.info("Successfully deleted file with token {}", fileToken);
    }
}
