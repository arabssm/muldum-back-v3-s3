package co.kr.muldum.s3.exception;

public class FileNotAttachedException extends RuntimeException {
    public FileNotAttachedException(String message) {
        super(message);
    }
}
