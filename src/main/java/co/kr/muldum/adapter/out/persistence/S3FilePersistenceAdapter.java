package co.kr.muldum.adapter.out.persistence;

import co.kr.muldum.core.domain.file.S3File;
import co.kr.muldum.core.domain.file.S3FileRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class S3FilePersistenceAdapter implements S3FileRepositoryPort {

    private final S3FileJpaRepository s3FileJpaRepository;

    @Override
    public S3File save(S3File s3File) {
        S3FileJpaEntity entity = new S3FileJpaEntity(
                s3File.getId(),
                s3File.getFileUrl(),
                s3File.getFileName(),
                s3File.getFileType(),
                s3File.getFileExtension(),
                s3File.getFileSize(),
                s3File.getUserId(),
                s3File.getUpdatedAt()
        );
        S3FileJpaEntity savedEntity = s3FileJpaRepository.save(entity);
        return new S3File(
                savedEntity.getId(),
                savedEntity.getFileUrl(),
                savedEntity.getFileName(),
                savedEntity.getFileType(),
                savedEntity.getFileExtension(),
                savedEntity.getFileSize(),
                savedEntity.getUserId(),
                savedEntity.getUpdatedAt()
        );
    }
}
