package com.ai.api.post.repository;

import com.ai.api.post.domain.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    // 특정 게시판의 게시글 목록 조회 (공지사항 우선)
    @Query("SELECT p FROM Post p WHERE p.board.id = :boardId ORDER BY p.isNotice DESC, p.createdAt DESC")
    Page<Post> findByBoardIdOrderByNotice(@Param("boardId") Long boardId, Pageable pageable);

    // 카테고리별 조회
    @Query("SELECT p FROM Post p WHERE p.board.id = :boardId AND p.category = :category ORDER BY p.isNotice DESC, p.createdAt DESC")
    Page<Post> findByBoardIdAndCategory(
        @Param("boardId") Long boardId,
        @Param("category") String category,
        Pageable pageable
    );

    // 제목 검색
    @Query("SELECT p FROM Post p WHERE p.board.id = :boardId AND p.title LIKE %:keyword% ORDER BY p.createdAt DESC")
    Page<Post> searchByTitle(
        @Param("boardId") Long boardId,
        @Param("keyword") String keyword,
        Pageable pageable
    );

    // 내용 검색
    @Query("SELECT p FROM Post p WHERE p.board.id = :boardId AND (p.title LIKE %:keyword% OR p.content LIKE %:keyword%) ORDER BY p.createdAt DESC")
    Page<Post> searchByTitleOrContent(
        @Param("boardId") Long boardId,
        @Param("keyword") String keyword,
        Pageable pageable
    );

    @Modifying
    @Query("UPDATE Post p SET p.view_count = COALESCE(p.view_count, 0) + 1 WHERE p.id = :id")
    void incrementViewCount(@Param("id") Long id);

}
