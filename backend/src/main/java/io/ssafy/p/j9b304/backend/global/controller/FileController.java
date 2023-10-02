package io.ssafy.p.j9b304.backend.global.controller;

import io.ssafy.p.j9b304.backend.global.entity.File;
import io.ssafy.p.j9b304.backend.global.service.FileService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/file")
@Tag(name = "파일 테스트", description = "파일 관련 API 문서")
public class FileController {
    private final FileService fileService;

    @PostMapping(value = "/upload", consumes = {"multipart/form-data"})
    public File uploadFile(@RequestPart(value = "file") MultipartFile multipartFile) {
        return fileService.addFile(multipartFile);
    }

    @DeleteMapping("/remove")
    public void removeFile(Long fileId) {
        fileService.removeFile(fileId);
    }
}
