package io.ssafy.p.j9b304.backend.global.service;

import io.ssafy.p.j9b304.backend.global.entity.File;
import io.ssafy.p.j9b304.backend.global.repository.FileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional
@RequiredArgsConstructor
public class FileService {
    private final S3Service s3Service;
    private final FileRepository fileRepository;

    public File addFile(MultipartFile multipartFile) {
        String originalFileName = multipartFile.getOriginalFilename();
        String fileUrl = s3Service.uploadFile(multipartFile);

        File file = new File();
        file.setOriginalName(originalFileName);
        file.setUrl(fileUrl);

        return fileRepository.save(file);
    }

    public void removeFile(Long fileId) {
        File file = fileRepository.findById(fileId)
                .orElseThrow(() -> new IllegalArgumentException("해당 파일이 없습니다."));

        s3Service.deleteFile(file.getUrl());
        fileRepository.delete(file);
    }
}
