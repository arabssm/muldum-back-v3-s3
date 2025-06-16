package co.kr.muldum.adapter.in.web;

import co.kr.muldum.adapter.in.web.dto.SaveFileRequest;
import co.kr.muldum.application.port.in.GeneratePresignedUrlUseCase;
import co.kr.muldum.application.port.in.SaveFileUseCase;
import co.kr.muldum.domain.file.File;
import co.kr.muldum.global.exception.CustomException;
import co.kr.muldum.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/files")
@RequiredArgsConstructor
public class FileController {

    private final GeneratePresignedUrlUseCase generatePresignedUrlUseCase;
    private final SaveFileUseCase saveFileUseCase;

    @GetMapping("/presigned")
    public ResponseEntity<String> getPresignedUrl(
            @RequestParam("fileName") String fileName
    ) {
        if (fileName == null) {
            throw new CustomException(ErrorCode.FILE_NOT_ATTACHED);
        }
        String url = generatePresignedUrlUseCase.generatePresignedUrl(fileName, "test-user");
        return ResponseEntity.ok(url);
    }

    @PostMapping
    public ResponseEntity<File> saveFile(
            @RequestBody SaveFileRequest request
    ) {
        File file = saveFileUseCase.save(
                request.getFileUrl(),
                request.getMetadata(),
                1L // test-user id
        );
        return ResponseEntity.ok(file);
    }
}
