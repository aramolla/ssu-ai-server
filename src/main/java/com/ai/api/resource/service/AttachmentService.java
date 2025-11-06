package com.ai.api.resource.service;

import com.ai.api.board.domain.Post;
import com.ai.api.resource.domain.Attachment;
import com.ai.api.resource.domain.PostAttachment;
import com.ai.api.resource.repository.AttachmentRepository;
import com.ai.api.resource.repository.PostAttachmentRepository;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Slf4j
public class AttachmentService {

    private final AttachmentRepository attachmentRepository;
    private final PostAttachmentRepository postAttachmentRepository;

    @Value("${file.Lab-research-upload-dir}")
    private String uploadDir;

    // 파일 조회
    public Attachment getAttachment(Long attachmentId){
        return attachmentRepository.findById(attachmentId)
            .orElseThrow(()->new RuntimeException("이미지를 찾을 수 없음"));
    }

    // 파일 단일 저장
    public Attachment saveAttachment(MultipartFile file) {
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

        } catch (Exception e) {
            log.error("file 업로드 로직 실패", e);
            throw new RuntimeException(e);
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

        for (MultipartFile file : files) {
            if (!file.isEmpty()) {
                try {
                    Attachment attachment = saveAttachment(file);

                    // PostAttachment 연결
                    PostAttachment postAttachment = PostAttachment.builder()
                        .post(post)
                        .attachment(attachment)
                        .build();
                    if(thumbnail != null && !thumbnail.isEmpty()){
                        postAttachment.setThumbnail(true);
                    }

                    postAttachmentRepository.save(postAttachment);

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

        } catch (Exception e) {
            log.info("파일 삭제 로직 실패", e);
            throw new RuntimeException(e);
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


    private String generateStoredFileName(String originalFileName) {
        String uuid = UUID.randomUUID().toString();
        String extention = "";

        if (originalFileName.contains(".")) {
            extention = originalFileName.substring(originalFileName.lastIndexOf("."));
        }
        return uuid + extention;
    }


}
