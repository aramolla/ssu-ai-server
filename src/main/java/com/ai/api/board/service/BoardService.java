package com.ai.api.board.service;

import com.ai.api.board.domain.Board;
import com.ai.api.board.dto.BoardReqDTO;
import com.ai.api.board.repository.BoardRepository;
import com.ai.api.resource.service.AttachmentService;
import com.ai.common.exception.EntityNotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;


    public List<Board> getAllBoards(Pageable pageable) {
        return boardRepository.findAll(pageable).getContent();
    }

    public Board getBoardByBoardEnName(String boardEnName) {
        return boardRepository.findByBoardEnName(boardEnName)
            .orElseThrow(() -> new EntityNotFoundException("Board not found with boardEnName: " + boardEnName));
    }

    public Board saveBoard(BoardReqDTO reqDTO) {
        // Builder를 사용하여 Board 엔티티 생성
        Board board = Board.builder()
            .title(reqDTO.getTitle())
            .content(reqDTO.getContent())
            .boardType(reqDTO.getBoardType())
            .pagingNum(reqDTO.getPagingNum() != null ? reqDTO.getPagingNum() : 10)
            .categories(reqDTO.getCategories())
            .sub1Label(reqDTO.getSub1Label())
            .sub2Label(reqDTO.getSub2Label())
            .sub3Label(reqDTO.getSub3Label())
            .sub4Label(reqDTO.getSub4Label())
            .sub5Label(reqDTO.getSub5Label())
            .build();

        log.info("새 게시판 메타데이터 저장: {}", reqDTO.getTitle());
        return boardRepository.save(board);
    }


    public Board updateBoard(String boardEnName, BoardReqDTO reqDTO) {
        Board board = boardRepository.findByBoardEnName(boardEnName)
            .orElseThrow(() -> new EntityNotFoundException("Board not found with boardEnName: " + boardEnName));

        // Dirty Checking을 이용하여 필드 업데이트
        board.setTitle(reqDTO.getTitle());
        board.setContent(reqDTO.getContent());
        board.setBoardType(reqDTO.getBoardType());

        if (reqDTO.getPagingNum() != null) {
            board.setPagingNum(reqDTO.getPagingNum());
        }
        board.setCategories(reqDTO.getCategories());
        board.setSub1Label(reqDTO.getSub1Label());
        board.setSub2Label(reqDTO.getSub2Label());
        board.setSub3Label(reqDTO.getSub3Label());
        board.setSub4Label(reqDTO.getSub4Label());
        board.setSub5Label(reqDTO.getSub5Label());

        log.info("게시판 메타데이터 수정 완료 (boardEnName: {})", boardEnName);
        return board;
    }


    public void deleteBoard(String boardEnName) {
        if (!boardRepository.existsByBoardEnName(boardEnName)) {
            throw new EntityNotFoundException("Board not found with boardEnName: " + boardEnName);
        }
        log.info("게시판 메타데이터 삭제 (boardEnName: {})", boardEnName);
        boardRepository.deleteByBoardEnName(boardEnName);
    }

}