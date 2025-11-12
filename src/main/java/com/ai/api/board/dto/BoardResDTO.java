package com.ai.api.board.dto;

import com.ai.api.board.domain.Board;
import com.ai.api.board.domain.BoardType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class BoardResDTO {

    private Long id;
    private String boardEnName;
    private String title;
    private String content;
    private BoardType boardType; // LIST, THUMBNAIIL, FAQ, COMPANY
    private String categories;
    private Integer pagingNum;

    private String sub1Label;
    private String sub2Label;
    private String sub3Label;
    private String sub4Label;
    private String sub5Label;


    public static BoardResDTO from(Board board) {
        return BoardResDTO.builder()
            .id(board.getId())
            .boardEnName(board.getBoardEnName())
            .title(board.getTitle())
            .content(board.getContent())
            .boardType(board.getBoardType())
            .pagingNum(board.getPagingNum())
            .sub1Label(board.getSub1Label())
            .sub2Label(board.getSub2Label())
            .sub3Label(board.getSub3Label())
            .sub4Label(board.getSub4Label())
            .sub5Label(board.getSub5Label())
            .build();
    }
}
