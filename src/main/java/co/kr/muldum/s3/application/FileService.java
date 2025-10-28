package co.kr.muldum.s3.application;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;

import java.net.URL;
import java.time.Duration;

@Service
@RequiredArgsConstructor
public class FileService {

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    @Value("${cloud.aws.region.static}")
    private String region;

    @Value("${cloud.aws.credentials.access-key}")
    private String accessKey;

    @Value("${cloud.aws.credentials.secret-key}")
    private String secretKey;

    public String generatePreSignedUrl(String fileName, String userId) {
        AwsBasicCredentials credentials = AwsBasicCredentials.create(accessKey, secretKey);

        try (S3Presigner presigner = S3Presigner.builder()
                .region(Region.of(region))
                .credentialsProvider(StaticCredentialsProvider.create(credentials))
                .build()) {

            // S3에 저장될 객체 키 (경로)
            String key = String.format("uploads/%s/%s", userId, fileName);

            PutObjectRequest objectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .contentType("application/octet-stream")
                    .build();

            // presigned URL 생성 (5분 유효)
            PresignedPutObjectRequest presignedRequest = presigner.presignPutObject(builder ->
                    builder.signatureDuration(Duration.ofMinutes(5))
                            .putObjectRequest(objectRequest));

            URL url = presignedRequest.url();
            return url.toString();
        }
    }
}
