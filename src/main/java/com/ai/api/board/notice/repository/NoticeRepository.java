package com.ai.api.board.notice.repository;

import com.ai.api.board.domain.Notice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface NoticeRepository extends JpaRepository<Notice, Long> {

    Page<Notice> findAll(Pageable pageable);
    Page<Notice> findByTitle(String title, Pageable pageable);

    @Modifying
    @Query("UPDATE Notice n SET n.view_count = COALESCE(n.view_count, 0) + 1 WHERE n.id = :id")
    void incrementViewCount(@Param("id") Long id);

}
