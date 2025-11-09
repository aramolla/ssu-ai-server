package com.ai.api.resource.dto;

import com.ai.api.resource.domain.Attachment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AttachmentDTO {
    private Long id;
    private String originalFileName;
    private Long fileSize;
    private String contentType;

    public static AttachmentDTO from(Attachment attachment){
        return AttachmentDTO.builder()
            .id(attachment.getId())
            .originalFileName(attachment.getOriginalFileName())
            .fileSize(attachment.getFileSize())
            .contentType(attachment.getContentType())
            .build();
    }
}