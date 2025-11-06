package com.ai.api.board.lab.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
@NoArgsConstructor @AllArgsConstructor //TODO
public class LabResearchReqDTO {
    private String title;
    private String content;
    private boolean isNotice;
    private String labSiteUrl;
    private MultipartFile thumbnail;
    private List<MultipartFile> attachments;

}
