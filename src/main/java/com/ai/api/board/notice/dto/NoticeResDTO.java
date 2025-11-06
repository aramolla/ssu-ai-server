package com.ai.api.board.notice.dto;

import com.ai.api.board.domain.Notice;
import java.time.LocalDateTime;
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

    public static NoticeResDTO from(Notice notice) {

        return NoticeResDTO.builder()
            .id(notice.getId())
            .title(notice.getTitle())
            .content(notice.getContent())
            .createdAt(notice.getCreatedAt())
            .viewCount(notice.getView_count())
            .build();
    }

}
