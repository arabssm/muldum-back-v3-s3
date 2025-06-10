package co.kr.muldum.application.port.in;

import co.kr.muldum.domain.file.File;
import co.kr.muldum.domain.file.FileMetadata;

import java.util.UUID;

public interface SaveFileUseCase {
    File save(String fileUrl, FileMetadata metadata, UUID userId);
}
