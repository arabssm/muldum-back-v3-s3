package co.kr.muldum.adapter.out.s3;

import co.kr.muldum.application.port.out.FileStoragePort;
import co.kr.muldum.infrastructure.config.S3Properties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Component
@RequiredArgsConstructor
public class S3StorageAdapter implements FileStoragePort {

    private final S3Client s3Client;
    private final S3Properties s3Properties;

    @Override
    public String uploadFile(String fileName, byte[] content, String contentType) {
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(s3Properties.getBucket())
                .key(fileName)
                .contentType(contentType)
                .build();
        s3Client.putObject(putObjectRequest, RequestBody.fromBytes(content));
        return s3Client.utilities().getUrl(builder -> builder.bucket(s3Properties.getBucket()).key(fileName)).toExternalForm();
    }

    @Override
    public byte[] downloadFile(String fileName) {
        // Not implemented yet
        return new byte[0];
    }

    @Override
    public void deleteFile(String fileName) {
        DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                .bucket(s3Properties.getBucket())
                .key(fileName)
                .build();
        s3Client.deleteObject(deleteObjectRequest);
    }
}
