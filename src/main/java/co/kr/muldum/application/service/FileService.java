package co.kr.muldum.application.service;

import co.kr.muldum.application.port.in.GeneratePresignedUrlUseCase;
import co.kr.muldum.application.port.in.SaveFileUseCase;
import co.kr.muldum.application.port.out.FileRepositoryPort;
import co.kr.muldum.domain.file.File;
import co.kr.muldum.domain.file.FileMetadata;
import co.kr.muldum.infrastructure.config.S3Properties;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;

import java.net.URL;
import java.time.Duration;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileService implements GeneratePresignedUrlUseCase, SaveFileUseCase {

    private final S3Properties s3Properties;
    private final FileRepositoryPort fileRepositoryPort;

    @Override
    public String generatePresignedUrl(String fileName, UUID userId) {
        AwsBasicCredentials credentials = AwsBasicCredentials.create(s3Properties.getAccessKey(), s3Properties.getSecretKey());

        try (S3Presigner presigner = S3Presigner.builder()
                .region(Region.of(s3Properties.getRegion()))
                .credentialsProvider(StaticCredentialsProvider.create(credentials))
                .build()) {

            String key = String.format("uploads/%s/%s", userId, fileName);

            PutObjectRequest objectRequest = PutObjectRequest.builder()
                    .bucket(s3Properties.getBucket())
                    .key(key)
                    .contentType("application/octet-stream")
                    .build();

            PresignedPutObjectRequest presignedRequest = presigner.presignPutObject(builder ->
                    builder.signatureDuration(Duration.ofMinutes(5))
                            .putObjectRequest(objectRequest));

            URL url = presignedRequest.url();
            return url.toString();
        }
    }

    @Override
    public File save(String fileUrl, FileMetadata metadata, UUID userId) {
        String extension = FilenameUtils.getExtension(metadata.getName());
        File file = File.create(fileUrl, metadata.getName(), metadata.getType(), extension, metadata.getSize_bytes(), userId);
        return fileRepositoryPort.save(file);
    }
}
