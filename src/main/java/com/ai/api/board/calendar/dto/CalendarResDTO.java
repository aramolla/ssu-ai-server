package com.ai.api.board.calendar.dto;

import com.ai.api.board.domain.Calendar;
import com.ai.api.resource.dto.AttachmentDTO;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CalendarResDTO {
    private Long id;
    private String title;
    private String content;
    private boolean isNotice;
    private LocalDate startedAt;
    private LocalDate endedAt;
    private LocalDateTime createdAt;
    private List<AttachmentDTO> attachments;

    public static CalendarResDTO from(Calendar calendar) {
        List<AttachmentDTO> attachmentDTOs = new ArrayList<>();
        if(calendar.getPostAttachments()!=null){
            attachmentDTOs = calendar.getPostAttachments()
                .stream()
                .map(pa -> AttachmentDTO.from(pa.getAttachment()))
                .collect(Collectors.toList());
        }
        return CalendarResDTO.builder()
            .id(calendar.getId())
            .title(calendar.getTitle())
            .content(calendar.getContent())
            .isNotice(calendar.isNotice())
            .startedAt(calendar.getStartedAt())
            .endedAt(calendar.getEndedAt())
            .createdAt(calendar.getCreatedAt())
            .attachments(attachmentDTOs)
            .build();
    }

}
