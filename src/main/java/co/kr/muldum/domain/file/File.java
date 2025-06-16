package co.kr.muldum.domain.file;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class File {

    @EqualsAndHashCode.Include
    private final Long id;

    private final String fileUrl;

    private final String fileName;

    private final String fileType;

    private final String fileExtension;

    private final Long fileSize;

    private final Long userId;

    private final LocalDateTime updatedAt;

    public static File create(String fileUrl, String fileName, String fileType, String fileExtension, Long fileSize, Long userId) {
        Objects.requireNonNull(fileUrl, "fileUrl must not be null");
        Objects.requireNonNull(fileName, "fileName must not be null");
        Objects.requireNonNull(userId, "userId must not be null");

        return new File(null, fileUrl, fileName, fileType, fileExtension, fileSize, userId, LocalDateTime.now());
    }

    public boolean isOwnedBy(Long userIdToCompare) {
        return this.userId.equals(userIdToCompare);
    }

    public boolean isImage() {
        return this.fileType != null && this.fileType.startsWith("image");
    }

    public File withId(Long id) {
        return new File(id, this.fileUrl, this.fileName, this.fileType, this.fileExtension, this.fileSize, this.userId, this.updatedAt);
    }
}
