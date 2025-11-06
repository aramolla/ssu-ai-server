package com.ai.api.board.notice.dto;

import com.ai.api.board.notice.domain.Category;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
@NoArgsConstructor @AllArgsConstructor
public class NoticeReqDTO {

    private Category category;
    private boolean isNotice;
    private String title;
    private String content;
    private List<MultipartFile> attachments;

}
