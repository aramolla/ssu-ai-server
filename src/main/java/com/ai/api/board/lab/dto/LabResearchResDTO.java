package com.ai.api.board.lab.dto;

import com.ai.api.board.domain.LabResearch;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor @AllArgsConstructor
public class LabResearchResDTO {
    private Long id;
    private String title;
    private boolean isNotice;
    private LocalDateTime createdAt;
    private String labSiteUrl;
    private String thumbnailUrl;
    private Integer viewCount;

    public static LabResearchResDTO from(LabResearch labResearch) {

        return LabResearchResDTO.builder()
            .id(labResearch.getId())
            .title(labResearch.getTitle())
            .isNotice(labResearch.isNotice())
            .createdAt(labResearch.getCreatedAt())
            .labSiteUrl(labResearch.getLabSiteUrl())
            .thumbnailUrl(labResearch.getThumbnail() != null ? labResearch.getThumbnail().getFilePath() : null)
            .viewCount(labResearch.getView_count())
            .build();
    }

}
