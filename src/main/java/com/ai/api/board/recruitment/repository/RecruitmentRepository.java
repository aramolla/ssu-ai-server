package com.ai.api.board.recruitment.repository;

import com.ai.api.board.domain.Recruitment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RecruitmentRepository extends JpaRepository<Recruitment, Long> {
    Page<Recruitment> findAll(Pageable pageable);
    Page<Recruitment> searchByTitle(@Param("keyword") String title, Pageable pageable);

    @Modifying
    @Query("UPDATE Recruitment r SET r.view_count = COALESCE(r.view_count, 0) + 1 WHERE r.id = :id")
    void incrementViewCount(@Param("id") Long id);

}
