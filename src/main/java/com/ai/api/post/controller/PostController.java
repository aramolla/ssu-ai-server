package com.ai.api.post.controller;

import com.ai.api.board.domain.Board;
import com.ai.api.board.domain.BoardCategory;
import com.ai.api.post.domain.Post;
import com.ai.api.post.dto.PostResDTO;
import com.ai.api.post.service.PostService;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/posts/{boardEnName}")
@RequiredArgsConstructor
@Slf4j
public class PostController {

    private final PostService postService;

    private BoardCategory getBoardCategory(String boardEnName) {
        return BoardCategory.fromEnName(boardEnName);
    }

    @GetMapping
    public ResponseEntity<List<PostResDTO>> getPosts(
        @PathVariable String boardEnName,
        @RequestParam(required = false) String category,
        @RequestParam(required = false) String keyword,
        @PageableDefault(sort = "id") Pageable pageable
    ) {
        BoardCategory boardCategory = getBoardCategory(boardEnName);
        log.info("{} 게시판 전체 조회", boardCategory.getTitle());

        Long boardId = boardCategory.getId();
        Board boardMetadata = postService.getBoardMetadata(boardId);

        log.info("Board ID: {}", boardId);
        log.info("Board Paging Num from DB: {}", boardMetadata.getPagingNum());

        int pageSize = boardMetadata.getPagingNum();

        Pageable userPageable = PageRequest.of(
            pageable.getPageNumber(),
            pageSize,
            pageable.getSort()
        );

        List<PostResDTO> postList = postService.getPosts(
                boardId, category, keyword, userPageable)
            .stream()
            .map(PostResDTO::from)
            .collect(Collectors.toList());

        return ResponseEntity.ok(postList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostResDTO> getPost(
        @PathVariable String boardEnName,
        @PathVariable Long id
    ) {
        BoardCategory boardCategory = getBoardCategory(boardEnName);
        log.info("{} 게시글 상세 조회(게시판 ID: {}, 게시글 ID: {})",
            boardCategory.getTitle(), boardCategory.getId(), id);

        Post post = postService.getPostById(id);
        return ResponseEntity.ok(PostResDTO.from(post));
    }
}