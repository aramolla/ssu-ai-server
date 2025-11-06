package com.ai.api.board.recruitment.dto;

import java.util.List;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class RecruitmentReqDTO {

    private boolean isNotice;
    private String title;
    private String content;
    private List<MultipartFile> attachments;

}
