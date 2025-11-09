package com.ai.api.board.dto;

import com.ai.api.board.domain.BoardType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class BoardReqDTO {

    private String title;
    private String content;
    private BoardType boardType; // LIST, THUMBNAIIL, FAQ, COMPANY
    private String categories; // JSON 문자열 형태
    private Integer pagingNum;

    private String sub1Label;
    private String sub2Label;
    private String sub3Label;
    private String sub4Label;
    private String sub5Label;

}
