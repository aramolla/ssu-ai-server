package com.ai.api.board.recruitment.dto;

import com.ai.api.board.dataroom.dto.DataRoomResDTO;
import com.ai.api.board.domain.DataRoom;
import com.ai.api.board.domain.Recruitment;
import com.ai.api.resource.dto.AttachmentDTO;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class RecruitmentResDTO {

    private Long id;
    private String title;
    private String content;

    private LocalDateTime createdAt;
    private Integer viewCount;

    private List<AttachmentDTO> attachments;

    public static RecruitmentResDTO from (Recruitment recruitment){
        List<AttachmentDTO> attachmentDTOs = new ArrayList<>();
        if(recruitment.getPostAttachments()!=null){
            attachmentDTOs = recruitment.getPostAttachments()
                .stream()
                .map(pa -> AttachmentDTO.from(pa.getAttachment()))
                .collect(Collectors.toList());
        }

        return RecruitmentResDTO.builder()
            .id(recruitment.getId())
            .title(recruitment.getTitle())
            .content(recruitment.getContent())
            .createdAt(recruitment.getCreatedAt())
            .viewCount(recruitment.getView_count())
            .attachments(attachmentDTOs)
            .build();
    }

}
