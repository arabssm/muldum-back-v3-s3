package co.kr.muldum.adapter.in.web;

import co.kr.muldum.adapter.in.web.dto.SaveFileRequest;
import co.kr.muldum.core.application.file.GeneratePresignedUrlUseCase;
import co.kr.muldum.core.application.file.SaveS3FileUseCase;
import co.kr.muldum.core.domain.file.S3File;
import co.kr.muldum.infrastructure.exception.FileNotAttachedException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/files")
@RequiredArgsConstructor
public class FileController {

    private final GeneratePresignedUrlUseCase generatePresignedUrlUseCase;
    private final SaveS3FileUseCase saveS3FileUseCase;

    @GetMapping("/presigned")
    public ResponseEntity<String> getPresignedUrl(
            @RequestParam("fileName") String fileName
    ) {
        if (fileName == null) {
            throw new FileNotAttachedException("파일이 첨부되지 않았습니다.");
        }
        String url = generatePresignedUrlUseCase.generatePresignedUrl(fileName, "test-user");
        return ResponseEntity.ok(url);
    }

    @PostMapping
    public ResponseEntity<S3File> saveS3File(
            @RequestBody SaveFileRequest request
    ) {
        S3File s3File = saveS3FileUseCase.save(
                request.getFileUrl(),
                request.getMetadata(),
                1L // test-user id
        );
        return ResponseEntity.ok(s3File);
    }
}
