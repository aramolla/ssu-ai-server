package com.ai.api.board.controller;

import com.ai.api.board.domain.Board;
import com.ai.api.board.dto.BoardReqDTO;
import com.ai.api.board.dto.BoardResDTO;
import com.ai.api.board.service.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/boards")
@RequiredArgsConstructor
@Slf4j
public class BoardController {

    private final BoardService boardService;

    @PostMapping
    public ResponseEntity<BoardResDTO> createBoard(@RequestBody BoardReqDTO reqDTO) {
        log.info("새 게시판 생성 요청: {}", reqDTO.getTitle());
        Board savedBoard = boardService.saveBoard(reqDTO);
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(BoardResDTO.from(savedBoard));
    }

    @GetMapping
    public ResponseEntity<List<BoardResDTO>> getAllBoards(
        @PageableDefault(sort = "id", size = 30) Pageable pageable) {

        List<BoardResDTO> boardList = boardService.getAllBoards(pageable).stream()
            .map(BoardResDTO::from)
            .collect(Collectors.toList());

        log.info("게시판 목록 조회 완료. 총 {}개", boardList.size());
        return ResponseEntity.ok(boardList);
    }


    @GetMapping("/{boardId}")
    public ResponseEntity<BoardResDTO> getBoardDetail(@PathVariable Long boardId) {
        Board board = boardService.getBoardById(boardId);
        log.info("게시판 상세 조회 완료 (ID: {})", boardId);
        return ResponseEntity.ok(BoardResDTO.from(board));
    }


    @PutMapping("/{boardId}")
    public ResponseEntity<BoardResDTO> updateBoard(@PathVariable Long boardId, @RequestBody BoardReqDTO reqDTO) {
        log.info("게시판 수정 요청 (ID: {})", boardId);
        Board updatedBoard = boardService.updateBoard(boardId, reqDTO);
        return ResponseEntity.ok(BoardResDTO.from(updatedBoard));
    }


    @DeleteMapping("/{boardId}")
    public ResponseEntity<Void> deleteBoard(@PathVariable Long boardId) {
        boardService.deleteBoard(boardId);
        log.warn("게시판 삭제 완료 (ID: {})", boardId);
        return ResponseEntity.noContent().build();
    }
}