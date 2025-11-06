package com.ai.api.board.dataroom.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
public class DataRoomReqDTO {

    private boolean isNotice;
    private String title;
    private String content;
    private List<MultipartFile> attachments;

}
