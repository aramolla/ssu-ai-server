package com.ai.api.post.controller.admin;

import com.ai.api.board.domain.BoardCategory;
import com.ai.api.post.domain.Post;
import com.ai.api.post.dto.PostReqDTO;
import com.ai.api.post.dto.PostResDTO;
import com.ai.api.post.service.PostService;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@Slf4j
public abstract class BasePostAdminController {

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

    @PostMapping
    public ResponseEntity<PostResDTO> createPost(
        @RequestPart("reqDTO") PostReqDTO reqDTO,
        @RequestPart(value = "thumbNail", required = false) MultipartFile image,
        @RequestPart(value = "attachments", required = false) List<MultipartFile> attachments
    ) {
        log.info("{} 게시글 저장", getBoardCategory().getTitle());
        reqDTO.setBoardId(getBoardCategory().getId());
        reqDTO.setThumbnail(image);
        reqDTO.setAttachments(attachments);
        Post savedPost = postService.createPost(reqDTO);

        return ResponseEntity.ok(PostResDTO.from(savedPost));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PostResDTO> updatePost(
        @PathVariable Long id,
        @RequestPart("reqDTO") PostReqDTO reqDTO,
        @RequestPart(value = "thumbNail", required = false) MultipartFile image,
        @RequestPart(value = "attachments", required = false) List<MultipartFile> attachments
    ) {
        log.info("{} 게시글 업데이트", getBoardCategory().getTitle());
        reqDTO.setBoardId(getBoardCategory().getId());
        reqDTO.setThumbnail(image);
        reqDTO.setAttachments(attachments);
        Post updatedPost = postService.updatePost(id, reqDTO);
        return ResponseEntity.ok(PostResDTO.from(updatedPost));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id) {
        log.info("{} 게시글 삭제", getBoardCategory().getTitle());
        postService.deletePost(id);
        return ResponseEntity.noContent().build();
    }

}
