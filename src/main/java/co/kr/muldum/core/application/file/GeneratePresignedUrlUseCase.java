package co.kr.muldum.core.application.file;

public interface GeneratePresignedUrlUseCase {
    String generatePresignedUrl(String fileName, String userId);
}
