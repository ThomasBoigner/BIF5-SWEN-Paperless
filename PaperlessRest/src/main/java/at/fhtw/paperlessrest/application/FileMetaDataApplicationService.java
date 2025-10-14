package at.fhtw.paperlessrest.application;

import at.fhtw.paperlessrest.application.dtos.FileMetaDataDto;
import at.fhtw.paperlessrest.domain.model.FileMetaDataRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

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
}
