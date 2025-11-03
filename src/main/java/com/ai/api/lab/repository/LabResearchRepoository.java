package com.ai.api.lab.repository;

import com.ai.api.lab.domain.LabResearch;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface LabResearchRepoository extends JpaRepository<LabResearch, Long> {

    Page<LabResearch> findAll(Pageable pageable);

    @Query("SELECT l FROM LabResearch l ORDER BY l.isNotice DESC, l.createdAt DESC") // 공지사항 우선
    Page<LabResearch> findAllOrderByNoticeAndCreatedAt(Pageable pageable);

    Page<LabResearch> searchByTitle(@Param("keyword") String title, Pageable pageable);


}
