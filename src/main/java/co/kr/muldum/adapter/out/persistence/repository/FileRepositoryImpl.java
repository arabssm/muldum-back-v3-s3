package co.kr.muldum.adapter.out.persistence.repository;

import co.kr.muldum.adapter.out.persistence.FileJpaEntity;
import co.kr.muldum.adapter.out.persistence.FileJpaRepository;
import co.kr.muldum.application.port.out.FileRepositoryPort;
import co.kr.muldum.domain.file.File;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class FileRepositoryImpl implements FileRepositoryPort {

    private final FileJpaRepository fileJpaRepository;

    @Override
    public File save(File file) {
        FileJpaEntity entity = new FileJpaEntity(
                file.getId(),
                file.getFileUrl(),
                file.getFileName(),
                file.getFileType(),
                file.getFileExtension(),
                file.getFileSize(),
                file.getUserId(),
                file.getUpdatedAt()
        );
        FileJpaEntity savedEntity = fileJpaRepository.save(entity);
        return file.withId(savedEntity.getId());
    }
}
