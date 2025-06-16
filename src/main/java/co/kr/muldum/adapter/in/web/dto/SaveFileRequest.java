package co.kr.muldum.adapter.in.web.dto;

import co.kr.muldum.domain.file.FileMetadata;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SaveFileRequest {
    private String fileUrl;
    private FileMetadata metadata;
}
