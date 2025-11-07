package com.ai.api.board.calendar.dto;

import java.time.LocalDate;
import java.util.List;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class CalendarReqDTO {
    private boolean isNotice;
    private String title;
    private String content;

    private LocalDate startedAt;
    private LocalDate endedAt;

    private List<MultipartFile> attachments;
}
