package co.kr.muldum.core.application.file;

import co.kr.muldum.core.domain.file.FileMetadata;
import co.kr.muldum.core.domain.file.S3File;

public interface SaveS3FileUseCase {
    S3File save(String fileUrl, FileMetadata metadata, Long userId);
}
