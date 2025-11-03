package com.ai.api.professor.repository;

import com.ai.api.professor.domain.Professor;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfessorRepository extends JpaRepository<Professor, Long> {
    Page<Professor> findAll(Pageable pageable);

    Optional<Professor> findById(Long Id);

    @Query("SELECT p FROM Professor p WHERE " +
        "p.professorName LIKE %:keyword% OR " +
        "p.professorEmail LIKE %:keyword% OR " +
        "p.department LIKE %:keyword% OR " +
        "p.major LIKE %:keyword% OR " +
        "p.office LIKE %:keyword% OR " +
        "p.tel LIKE %:keyword%")
    Page<Professor> searchByKeyword(@Param("keyword") String keyword, Pageable pageable);

}
