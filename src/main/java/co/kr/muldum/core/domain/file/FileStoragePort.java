package co.kr.muldum.core.domain.file;

public interface FileStoragePort {
    String uploadFile(String fileName, byte[] content, String contentType);
    byte[] downloadFile(String fileName);
    void deleteFile(String fileName);
}
