package co.kr.muldum.application.port.in;

public interface GeneratePresignedUrlUseCase {
    String generatePresignedUrl(String fileName, String userId);
}
