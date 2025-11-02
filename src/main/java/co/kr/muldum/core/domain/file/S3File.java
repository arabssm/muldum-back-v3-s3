package co.kr.muldum.core.domain.file;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class S3File {
    private Long id;
    private String fileUrl;
    private String fileName;
    private String fileType;
    private String fileExtension;
    private Long fileSize;
    private Long userId;
    private LocalDateTime updatedAt;
}
