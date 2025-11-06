package com.ai.api.board.dataroom.dto;

import com.ai.api.board.domain.DataRoom;
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
public class DataRoomResDTO {
    private Long id;
    private String title;
    private String content;

    private LocalDateTime createdAt;
    private Integer viewCount;

    private List<AttachmentDTO> attachments;

    public static DataRoomResDTO from (DataRoom dataRoom){
        List<AttachmentDTO> attachmentDTOs = new ArrayList<>();
        if(dataRoom.getPostAttachments()!=null){
            attachmentDTOs = dataRoom.getPostAttachments()
                .stream()
                .map(pa -> AttachmentDTO.from(pa.getAttachment()))
                .collect(Collectors.toList());
        }

        return DataRoomResDTO.builder()
            .id(dataRoom.getId())
            .title(dataRoom.getTitle())
            .content(dataRoom.getContent())
            .createdAt(dataRoom.getCreatedAt())
            .viewCount(dataRoom.getView_count())
            .attachments(attachmentDTOs)
            .build();
    }


}
