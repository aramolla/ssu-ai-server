package com.ai.api.resource.service;

import com.ai.api.post.domain.Post;
import com.ai.api.resource.domain.Attachment;
import com.ai.api.resource.domain.PostAttachment;
import com.ai.api.resource.repository.AttachmentRepository;
import com.ai.api.resource.repository.PostAttachmentRepository;
import com.ai.common.exception.EntityNotFoundException;
import com.ai.common.exception.FileStorageException;
import com.ai.common.exception.InvalidFileException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class AttachmentService {

    private final AttachmentRepository attachmentRepository;
    private final PostAttachmentRepository postAttachmentRepository;

    private static final long MAX_FILE_SIZE = 300 * 1024; // 300KB
    private static final List<String> ALLOWED_EXTENSIONS = Arrays.asList(
        "jpg", "jpeg", "png", "gif", "pdf", "doc", "docx", "xls", "xlsx", "ppt", "pptx", ".png"
    );

    @Value("${file.Lab-research-upload-dir}")
    private String uploadDir;

    // 파일 조회
    public Attachment getAttachment(Long attachmentId){
        return attachmentRepository.findById(attachmentId)
            .orElseThrow(()->new EntityNotFoundException("이미지를 찾을 수 없음"));
    }

    // 파일 단일 저장( 썸네일 저장 )
    public Attachment saveAttachment(MultipartFile file) {
        validateFile(file);
        Path path = Paths.get(uploadDir);
        try{
            String originalName = file.getOriginalFilename();
            String storedName = generateStoredFileName(originalName);
            Path filePath = path.resolve(storedName);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING); // 파일 저장

            Attachment savefile = Attachment.builder()
                .originalFileName(originalName)
                .storedFileName(storedName)
                .FilePath(filePath.toString())
                .FileSize(file.getSize())
                .contentType(file.getContentType())
                .build();

            Attachment saveImage = attachmentRepository.save(savefile);

            log.info("file 저장 로직 완료 {}", filePath);

            return saveImage;

        } catch (IOException e) {
            log.error("파일 업로드 실패: {}", file.getOriginalFilename(), e);
            throw new FileStorageException("파일 저장에 실패했습니다: " + file.getOriginalFilename(), e);
        }
    }

    // 썸네일 여부 정보 Post와 연결
    public void saveThumbnailAttachment(Attachment thumbnail, Post post) {
        PostAttachment postAttachment = new PostAttachment();
        if (thumbnail == null || post == null) {
            postAttachment.setThumbnail(false);
        }

        postAttachment.setThumbnail(true);

        postAttachmentRepository.save(postAttachment);

        log.info("게시글({})에 썸네일({}) 연결 완료", post.getId(), thumbnail.getOriginalFileName());
    }

    // 파일 리스트 저장 및 Post와 연결
    public void savePostAttachments(MultipartFile thumbnail, List<MultipartFile> files, Post post) {
        if (files == null || files.isEmpty()) {
            return;
        }

        if (post.getPostAttachments() == null) {
            post.setPostAttachments(new ArrayList<>());
        }

        for (MultipartFile file : files) {
            if (!file.isEmpty()) {
                try {
                    Attachment attachment = saveAttachment(file);

                    // PostAttachment 연결
                    PostAttachment postAttachment = PostAttachment.builder()
                        .post(post)
                        .attachment(attachment)
                        .isThumbnail(thumbnail != null && !thumbnail.isEmpty())
                        .build();

                    postAttachmentRepository.save(postAttachment);

                    post.getPostAttachments().add(postAttachment); // 양방향 관계 직접 설정

                    log.info("게시글({})에 첨부파일({}) 연결 완료", post.getId(), attachment.getOriginalFileName());

                } catch (Exception e) {
                    log.error("첨부파일 저장 실패: {}", file.getOriginalFilename(), e);
                }
            }
        }
    }


    public void deleteAttachment(Long attachmentId){
        Attachment existingAttachment = attachmentRepository.findById(attachmentId)
            .orElseThrow(() -> new RuntimeException("파일을 찾을 수 없음"));

        if(existingAttachment.getPostAttachment()!=null){
            existingAttachment.getPostAttachment().clear();
        }

        try {
            Path AttachmentPath = Paths.get(existingAttachment.getFilePath());
            Files.deleteIfExists(AttachmentPath); // 물리적으로 파일 삭제, 파일이 존재 위에서 확인하여 예외던지지 않음
            attachmentRepository.delete(existingAttachment);
            log.info("파일 삭제 로직 성공");

        } catch (IOException e) {
            log.error("파일 삭제 실패: {}", existingAttachment.getFilePath(), e);
            throw new FileStorageException("파일 삭제에 실패했습니다: " + existingAttachment.getOriginalFileName(), e);
        }
    }

    public void deletePostAttachments(Post post) {
        if (post.getPostAttachments() == null || post.getPostAttachments().isEmpty()) {
            return;
        }
        List<PostAttachment> attachmentsToDelete = new java.util.ArrayList<>(post.getPostAttachments());

        post.getPostAttachments().clear(); // post에서 PostAttachments 먼저 지우기

        for (PostAttachment pa : attachmentsToDelete) {
            try {
                Attachment attachment = pa.getAttachment();
                if (attachment != null) {
                    deleteAttachment(attachment.getId());
                }
            } catch (Exception e) {
                log.error("첨부파일 본체 삭제 실패: {}", pa.getAttachment() != null ? pa.getAttachment().getId() : "null", e);
            }
        }
    }

    private void validateFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new InvalidFileException("파일이 비어있습니다");
        }

        if (file.getSize() > MAX_FILE_SIZE) {
            throw new InvalidFileException(
                String.format("파일 크기가 제한을 초과했습니다: %dKB (최대: 300KB)",
                    file.getSize() / 1024));
        }

        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || originalFilename.contains("..")) {
            throw new InvalidFileException("잘못된 파일명입니다");
        }

        String extension = getFileExtension(originalFilename).toLowerCase();
        if (!ALLOWED_EXTENSIONS.contains(extension)) {
            throw new InvalidFileException(
                String.format("허용되지 않은 파일 형식입니다: %s (허용: %s)",
                    extension, String.join(", ", ALLOWED_EXTENSIONS)));
        }

        String contentType = file.getContentType();
        if (contentType == null) {
            throw new InvalidFileException("파일 타입을 확인할 수 없습니다");
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

    private String getFileExtension(String filename) {
        if (filename == null || !filename.contains(".")) {
            return "";
        }
        return filename.substring(filename.lastIndexOf("."));
    }


}
