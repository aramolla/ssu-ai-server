package com.ai.api.post.controller;

import com.ai.api.board.domain.BoardCategory;
import com.ai.api.post.domain.Post;
import com.ai.api.post.dto.PostResDTO;
import com.ai.api.post.service.PostService;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@Slf4j
public abstract class BasePostController {

    private final PostService postService;
    protected abstract BoardCategory getBoardCategory();

    @GetMapping
    public ResponseEntity<List<PostResDTO>> getPosts(
        @RequestParam(required = false) String category,
        @RequestParam(required = false) String keyword,
        @PageableDefault(sort = "id") Pageable pageable
    ) {
        log.info("{} 게시판 전체 조회", getBoardCategory().getTitle());
        List<PostResDTO> postList = postService.getPosts(
                getBoardCategory().getId(), category, keyword, pageable)
            .stream()
            .map(PostResDTO::from)
            .collect(Collectors.toList());

        return ResponseEntity.ok(postList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostResDTO> getPost(@PathVariable Long id) {
        log.info("{} 게시글 상세 조회(게시판 ID: {}, 게시글 ID: {})",
            getBoardCategory().getTitle(), getBoardCategory().getId(), id);
        Post post = postService.getPostById(id);
        return ResponseEntity.ok(PostResDTO.from(post));
    }

}