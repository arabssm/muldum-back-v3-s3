package co.kr.muldum.application.port.in;

import java.util.UUID;

public interface GeneratePresignedUrlUseCase {
    String generatePresignedUrl(String fileName, UUID userId);
}
