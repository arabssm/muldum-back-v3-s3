package co.kr.muldum.adapter.out.persistence;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "s3file")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class S3FileJpaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String fileUrl;

    private String fileName;

    private String fileType;

    private String fileExtension;

    private Long fileSize;

    private Long userId;

    @Column(nullable = false)
    private LocalDateTime updatedAt;
}
