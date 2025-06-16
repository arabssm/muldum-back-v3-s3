package co.kr.muldum.application.port.in;

import co.kr.muldum.domain.file.File;
import co.kr.muldum.domain.file.FileMetadata;

public interface SaveFileUseCase {
    File save(String fileUrl, FileMetadata metadata, Long userId);
}
