package com.ai.api.resource.service;

import com.ai.api.professor.domain.ProfessorImage;
import com.ai.api.professor.repository.ProfessorImageRepository;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class FileService {

    private final ProfessorImageRepository professorImageRepository;

    @Value("${file.upload-dir}")
    private String uploadDir;

    // 파일 조회
    public ProfessorImage getImage(Long imageId){
        return professorImageRepository.findById(imageId)
            .orElseThrow(()->new RuntimeException("이미지를 찾을 수 없음"));
    }

    public ProfessorImage saveFile(MultipartFile image) {
        Path path = Paths.get(uploadDir);
        try{
            String originalImageName = image.getOriginalFilename();
            String storedImageName = generateStoredFileName(originalImageName);
            Path imagePath = path.resolve(storedImageName);
            Files.copy(image.getInputStream(), imagePath, StandardCopyOption.REPLACE_EXISTING); // 파일 저장

            ProfessorImage professorImage = ProfessorImage.builder()
                .originalImageName(originalImageName)
                .storedImageName(storedImageName)
                .imagePath(imagePath.toString())
                .imageSize(image.getSize())
                .contentType(image.getContentType())
                .build();

            ProfessorImage saveImage = professorImageRepository.save(professorImage);

            log.info("이미지 저장 로직 완료 {}", imagePath);

            return saveImage;

        } catch (Exception e) {
            log.error("이미지 업로드 로직 실패", e);
            throw new RuntimeException(e);
        }
    }

    public void deleteImage(Long imageId){
        ProfessorImage existingImage = professorImageRepository.findById(imageId)
            .orElseThrow(() -> new RuntimeException("파일을 찾을 숭 없음"));

        try {
            Path imagePath = Paths.get(existingImage.getImagePath());
            Files.deleteIfExists(imagePath); // 물리적으로 파일 삭제, 파일이 존재 위에서 확인하여 예외던지지 않음
            professorImageRepository.delete(existingImage);
            log.info("파일 삭제 로직 성곤");

        } catch (Exception e) {
            log.info("파일 삭제 로직 실패", e);
            throw new RuntimeException(e);
        }
    }

    private String generateStoredFileName(String originalFileName) {
        String uuid = UUID.randomUUID().toString();
        String extention = "";

        if (originalFileName.contains(".")) {
            extention = originalFileName.substring(originalFileName.lastIndexOf("."));
        }
        return uuid + extention;
    }



}
