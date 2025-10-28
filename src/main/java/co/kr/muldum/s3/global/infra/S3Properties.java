package co.kr.muldum.s3.global.infra;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Setter
@Getter
@Configuration
@ConfigurationProperties("spring.cloud.aws.s3")
public class S3Properties {
    private String bucket;
    private String accessKey;
    private String secretKey;
    private String region;
}