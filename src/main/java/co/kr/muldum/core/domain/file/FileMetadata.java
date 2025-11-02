package co.kr.muldum.core.domain.file;

import lombok.Value;

@Value(staticConstructor = "of")
public class FileMetadata {
    String name;
    String type;
    long size_bytes;
}
