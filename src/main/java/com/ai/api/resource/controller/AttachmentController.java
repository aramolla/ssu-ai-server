package com.ai.api.resource.controller;

import com.ai.api.resource.domain.Attachment;
import com.ai.api.resource.service.AttachmentService;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/attachment")
@RequiredArgsConstructor
@Slf4j
public class AttachmentController {
    private final AttachmentService attachmentService;

    @GetMapping("/download/{attachmentId}")
    public ResponseEntity<Resource> download(@PathVariable("attachmentId") Long attachmentId) {
        try {
            Attachment attachment = attachmentService.getAttachment(attachmentId);
            Path path = Paths.get(attachment.getFilePath());
            Resource resource = new UrlResource(path.toUri());

            String originalFileName = attachment.getOriginalFileName();
            String encodedFileName = URLEncoder.encode(originalFileName, StandardCharsets.UTF_8)
                .replaceAll("\\+", "%20");


            if (!resource.exists()) {
                return ResponseEntity.notFound().build();
            }

            String contentType = attachment.getContentType();
            if (contentType == null) {
                contentType = "application/octet-stream";
            }

            return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename*=UTF-8''" + encodedFileName)
                .body(resource);
        } catch (Exception e) {
            log.error("파일 다운로드 실패: {}", attachmentId, e);
            return ResponseEntity.notFound().build();

        }
    }

}
