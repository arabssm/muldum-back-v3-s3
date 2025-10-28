package co.kr.muldum.s3.presentation;

import co.kr.muldum.s3.application.FileService;
import co.kr.muldum.s3.exception.FileNotAttachedException;
import co.kr.muldum.s3.global.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/files")
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;

    @GetMapping("/presigned")
    public ResponseEntity<String> getPresignedUrl(
            @RequestParam("fileName") String fileName,
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        if (fileName == null) {
            throw new FileNotAttachedException("파일이 첨부되지 않았습니다.");
        }
        String url = fileService.generatePreSignedUrl(fileName, user.getUserId());
        return ResponseEntity.ok(url);
    }
}
