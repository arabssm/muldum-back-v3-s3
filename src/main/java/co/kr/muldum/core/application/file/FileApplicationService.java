package co.kr.muldum.core.application.file;

import co.kr.muldum.core.domain.file.FileMetadata;
import co.kr.muldum.core.domain.file.S3File;
import co.kr.muldum.core.domain.file.S3FileRepositoryPort;
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
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class FileApplicationService implements GeneratePresignedUrlUseCase, SaveS3FileUseCase {

    private final S3Properties s3Properties;
    private final S3FileRepositoryPort s3FileRepositoryPort;

    @Override
    public String generatePresignedUrl(String fileName, String userId) {
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
    public S3File save(String fileUrl, FileMetadata metadata, Long userId) {
        String extension = FilenameUtils.getExtension(metadata.getName());
        S3File s3File = new S3File(null, fileUrl, metadata.getName(), metadata.getType(), extension, metadata.getSize_bytes(), userId, LocalDateTime.now());
        return s3FileRepositoryPort.save(s3File);
    }
}
