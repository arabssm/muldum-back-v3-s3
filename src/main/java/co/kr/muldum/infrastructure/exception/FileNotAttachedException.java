package co.kr.muldum.infrastructure.exception;

public class FileNotAttachedException extends RuntimeException {
    public FileNotAttachedException(String message) {
        super(message);
    }
}
