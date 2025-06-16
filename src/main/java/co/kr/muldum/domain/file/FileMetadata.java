package co.kr.muldum.domain.file;

import lombok.Value;

@Value(staticConstructor = "of")
public class FileMetadata {
    String name;
    String type;
    long size_bytes;
}
