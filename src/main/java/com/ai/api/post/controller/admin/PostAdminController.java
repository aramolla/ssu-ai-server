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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/admin/posts/{boardEnName}")
@RequiredArgsConstructor
@Slf4j
public class PostAdminController {

    private final PostService postService;

    private BoardCategory getBoardCategory(String boardEnName) {
        return BoardCategory.fromEnName(boardEnName);
    }

    @GetMapping
    public ResponseEntity<Page<PostResDTO>> getPosts(
        @PathVariable String boardEnName,
        @RequestParam(required = false) String category,
        @RequestParam(required = false) String keyword,
        @PageableDefault(sort = "id") Pageable pageable
    ) {
        BoardCategory boardCategory = getBoardCategory(boardEnName);
        log.info("{} 게시판 전체 조회", boardCategory.getTitle());

        Page<Post> postPage = postService.getPosts(
                boardCategory.getId(), category, keyword, pageable);

        Page<PostResDTO> postResPage = postPage.map(PostResDTO::from);

        return ResponseEntity.ok(postResPage);
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

    @PostMapping
    public ResponseEntity<PostResDTO> createPost(
        @PathVariable String boardEnName,
        @RequestPart("reqDTO") PostReqDTO reqDTO,
        @RequestPart(value = "thumbNail", required = false) MultipartFile image,
        @RequestPart(value = "attachments", required = false) List<MultipartFile> attachments
    ) {
        BoardCategory boardCategory = getBoardCategory(boardEnName);
        log.info("{} 게시글 저장", boardCategory.getTitle());

        reqDTO.setBoardId(boardCategory.getId());
        reqDTO.setThumbnail(image);
        reqDTO.setAttachments(attachments);
        Post savedPost = postService.createPost(reqDTO);

        return ResponseEntity.ok(PostResDTO.from(savedPost));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PostResDTO> updatePost(
        @PathVariable String boardEnName,
        @PathVariable Long id,
        @RequestPart("reqDTO") PostReqDTO reqDTO,
        @RequestPart(value = "thumbNail", required = false) MultipartFile image,
        @RequestPart(value = "attachments", required = false) List<MultipartFile> attachments
    ) {
        BoardCategory boardCategory = getBoardCategory(boardEnName);
        log.info("{} 게시글 업데이트", boardCategory.getTitle());

        reqDTO.setBoardId(boardCategory.getId());
        reqDTO.setThumbnail(image);
        reqDTO.setAttachments(attachments);
        Post updatedPost = postService.updatePost(id, reqDTO);
        return ResponseEntity.ok(PostResDTO.from(updatedPost));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(
        @PathVariable String boardEnName,
        @PathVariable Long id
    ) {
        BoardCategory boardCategory = getBoardCategory(boardEnName);
        log.info("{} 게시글 삭제", boardCategory.getTitle());

        postService.deletePost(id);
        return ResponseEntity.noContent().build();
    }
}