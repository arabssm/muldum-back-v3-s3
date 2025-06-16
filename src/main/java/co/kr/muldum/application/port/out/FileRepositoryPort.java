package co.kr.muldum.application.port.out;

import co.kr.muldum.domain.file.File;

public interface FileRepositoryPort {
    File save(File file);
}
