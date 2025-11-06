package com.ai.api.board.research.repository;

import com.ai.api.board.domain.Research;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ResearchRepository extends JpaRepository<Research, Long> {

    Page<Research> findAll(Pageable pageable);
    Page<Research> searchByTitle(@Param("keyword") String keyword, Pageable pageable);

    @Modifying
    @Query("UPDATE Research r SET r.view_count = COALESCE(r.view_count, 0) + 1 WHERE r.id = :id")
    void incrementViewCount(@Param("id") Long id);

}
