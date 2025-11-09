package com.ai.api.post.dto;

import com.ai.api.resource.domain.PostAttachment;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Map;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostReqDTO {

    private Long boardId;
    private String title;
    private String content;
    private boolean isNotice;


    private Map<String, String> dynamicFields;

    private String category;

    private MultipartFile thumbnail;
    private List<MultipartFile> attachments;

}
