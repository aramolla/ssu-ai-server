package com.ai.api.post.service;

import com.ai.api.board.domain.Board;
import com.ai.api.board.domain.BoardType;
import com.ai.api.post.domain.Post;
import com.ai.api.post.dto.PostReqDTO;
import com.ai.api.board.repository.BoardRepository;
import com.ai.api.post.repository.PostRepository;
import com.ai.api.resource.domain.Attachment;
import com.ai.api.resource.service.AttachmentService;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final BoardRepository boardRepository;
    private final AttachmentService attachmentService;

    // BoardType 수정은 Post가 아닌 Board에서 진행됨
    // post에서는 모든 값을 저장하고 BoardType 따른 처리는 프론트에서 표시해줄 반환값에서 수행

    public Post createPost(PostReqDTO reqDTO) {
        Board board = boardRepository.findById(reqDTO.getBoardId())
            .orElseThrow(() -> new IllegalArgumentException("Board not found with ID: " + reqDTO.getBoardId()));

        // Board 설정에 따라 동적으로 Post 생성
        Post.PostBuilder<?, ?> builder = Post.builder()
            .board(board)
            .title(reqDTO.getTitle())
            .content(reqDTO.getContent())
            .isNotice(reqDTO.isNotice())
            .category(reqDTO.getCategory())
            .startedAt(reqDTO.getStartedAt())
            .endedAt(reqDTO.getEndedAt())
            .view_count(0);

        if (reqDTO.getThumbnail() != null) {
            Attachment saveImage = attachmentService.saveAttachment(reqDTO.getThumbnail());
            builder.thumbnail(saveImage);
        }

        mapDynamicFields(board, reqDTO.getDynamicFields(), builder);

        Post post = builder.build();
        post = postRepository.save(post);

        if(reqDTO.getAttachments() != null) {
            attachmentService.savePostAttachments(reqDTO.getThumbnail(), reqDTO.getAttachments(), post);
        }

        log.info("게시글 생성 (게시판: {}, 제목: {})", board.getTitle(), reqDTO.getTitle());
        return postRepository.save(post);
    }

    private void mapDynamicFields(Board board, Map<String, String> dynamicFields, Post.PostBuilder<?, ?> builder) {
        if (dynamicFields == null || dynamicFields.isEmpty()) {
            return;
        }
        // Board의 label과 요청의 key를 매칭하여 subValue에 저장
        if (board.getSub1Label() != null && dynamicFields.containsKey(board.getSub1Label())) {
            builder.sub1Value(dynamicFields.get(board.getSub1Label()));
        }
        if (board.getSub2Label() != null && dynamicFields.containsKey(board.getSub2Label())) {
            builder.sub2Value(dynamicFields.get(board.getSub2Label()));
        }
        if (board.getSub3Label() != null && dynamicFields.containsKey(board.getSub3Label())) {
            builder.sub3Value(dynamicFields.get(board.getSub3Label()));
        }
        if (board.getSub4Label() != null && dynamicFields.containsKey(board.getSub4Label())) {
            builder.sub4Value(dynamicFields.get(board.getSub4Label()));
        }
        if (board.getSub5Label() != null && dynamicFields.containsKey(board.getSub5Label())) {
            builder.sub5Value(dynamicFields.get(board.getSub5Label()));
        }
    }

    public Page<Post> getPosts(Long boardId, String category, String keyword, Pageable pageable) {
        // 카테고리와 키워드 모두 있는 경우
        if (category != null && !category.isEmpty() && keyword != null && !keyword.isEmpty()) {
            // 복합 검색은 별도 쿼리 필요 (여기서는 카테고리 우선)
            return postRepository.findByBoardIdAndCategory(boardId, category, pageable);
        }
        // 카테고리만 있는 경우
        else if (category != null && !category.isEmpty()) {
            return postRepository.findByBoardIdAndCategory(boardId, category, pageable);
        }
        // 키워드만 있는 경우
        else if (keyword != null && !keyword.isEmpty()) {
            return postRepository.searchByTitleOrContent(boardId, keyword, pageable);
        }
        // 전체 조회
        else {
            return postRepository.findByBoardIdOrderByNotice(boardId, pageable);
        }
    }

    public Post getPostById(Long id) {
        postRepository.incrementViewCount(id);
        return postRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Post not found with ID: " + id));
    }


    public Post updatePost(Long id, PostReqDTO reqDTO) {
        Post post = postRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Post not found with ID: " + id));
        Board board = post.getBoard();

        // 필드 업데이트
        post.setTitle(reqDTO.getTitle());
        post.setContent(reqDTO.getContent());
        post.setNotice(reqDTO.isNotice());
        post.setCategory(reqDTO.getCategory());
        post.setStartedAt(reqDTO.getStartedAt());
        post.setEndedAt(reqDTO.getEndedAt());
        if (reqDTO.getDynamicFields() != null) {
            if (board.getSub1Label() != null && reqDTO.getDynamicFields().containsKey(board.getSub1Label())) {
                post.setSub1Value(reqDTO.getDynamicFields().get(board.getSub1Label()));
            }
            if (board.getSub2Label() != null && reqDTO.getDynamicFields().containsKey(board.getSub2Label())) {
                post.setSub2Value(reqDTO.getDynamicFields().get(board.getSub2Label()));
            }
            if (board.getSub3Label() != null && reqDTO.getDynamicFields().containsKey(board.getSub3Label())) {
                post.setSub3Value(reqDTO.getDynamicFields().get(board.getSub3Label()));
            }
            if (board.getSub4Label() != null && reqDTO.getDynamicFields().containsKey(board.getSub4Label())) {
                post.setSub4Value(reqDTO.getDynamicFields().get(board.getSub4Label()));
            }
            if (board.getSub5Label() != null && reqDTO.getDynamicFields().containsKey(board.getSub5Label())) {
                post.setSub5Value(reqDTO.getDynamicFields().get(board.getSub5Label()));
            }
        }

        if (reqDTO.getThumbnail() != null) {
            post.setThumbnail(attachmentService.saveAttachment(reqDTO.getThumbnail()));
        }
        if(reqDTO.getAttachments() != null) {
            attachmentService.deletePostAttachments(post);
            attachmentService.savePostAttachments(reqDTO.getThumbnail(), reqDTO.getAttachments(), post);
        }

        log.info("게시글 수정 완료 (ID: {})", id);

        Post updatePost = postRepository.save(post);

        return updatePost;
    }

    /**
     * 게시글 삭제
     */
    public void deletePost(Long id) {
        Post post = postRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Post not found with ID: " + id));

        if(post.getThumbnail() != null) {
            attachmentService.deleteAttachment(post.getThumbnail().getId());
        }

        if(post.getPostAttachments() != null) {
            attachmentService.deletePostAttachments(post);
        }
        log.info("게시글 삭제 (ID: {})", id);
        postRepository.deleteById(id);
    }

    /**
     * 게시판 메타데이터 조회 (클라이언트가 폼을 그릴 때 사용)
     */
    public Board getBoardMetadata(Long boardId) {
        return boardRepository.findById(boardId)
            .orElseThrow(() -> new IllegalArgumentException("Board not found with ID: " + boardId));
    }


}
