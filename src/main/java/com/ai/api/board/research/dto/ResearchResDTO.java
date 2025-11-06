package com.ai.api.board.research.dto;

import com.ai.api.board.domain.Research;
import com.ai.api.resource.dto.AttachmentDTO;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResearchResDTO {

    private Long id;
    private String title;
    private String content;

    private LocalDateTime createdAt;
    private Integer viewCount;

    private List<AttachmentDTO> attachments;


    public static ResearchResDTO from(Research research) {
        List<AttachmentDTO> attachmentDTOs = new ArrayList<>();
        if (research.getPostAttachments() != null){
            attachmentDTOs = research.getPostAttachments()
                .stream()
                .map(pa -> AttachmentDTO.from((pa.getAttachment())))
                .collect(Collectors.toList());
        }
        return ResearchResDTO.builder()
            .id(research.getId())
            .title(research.getTitle())
            .content(research.getContent())
            .createdAt(research.getCreatedAt())
            .viewCount(research.getView_count())
            .attachments(attachmentDTOs)
            .build();
    }



}
