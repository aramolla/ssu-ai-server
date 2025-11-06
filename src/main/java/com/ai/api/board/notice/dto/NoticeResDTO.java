package com.ai.api.board.notice.dto;

import com.ai.api.board.domain.Notice;
import com.ai.api.resource.dto.AttachmentDTO;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NoticeResDTO {
    private Long id;
    private String title;
    private String content;
    private LocalDateTime createdAt;
    private Integer viewCount;
    private List<AttachmentDTO> attachments;

    public static NoticeResDTO from(Notice notice) {
        List<AttachmentDTO> attachmentDTOS = new ArrayList<>();
        if(notice.getPostAttachments()!=null){
            attachmentDTOS = notice.getPostAttachments()
                .stream()
                .map(pa -> AttachmentDTO.from(pa.getAttachment()))// , pa.isThumbnail()))
                .collect(Collectors.toList());
        }

        return NoticeResDTO.builder()
            .id(notice.getId())
            .title(notice.getTitle())
            .content(notice.getContent())
            .createdAt(notice.getCreatedAt())
            .viewCount(notice.getView_count())
            .attachments(attachmentDTOS)
            .build();
    }

}
