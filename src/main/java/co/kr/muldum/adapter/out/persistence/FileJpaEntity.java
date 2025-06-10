package co.kr.muldum.adapter.out.persistence;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "file")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class FileJpaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String fileUrl;

    private String fileName;

    private String fileType;

    private String fileExtension;

    private Long fileSize;

    @JdbcTypeCode(SqlTypes.VARCHAR)
    private UUID userId;

    @Column(nullable = false)
    private LocalDateTime updatedAt;
}
