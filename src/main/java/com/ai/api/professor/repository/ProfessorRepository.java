package com.ai.api.professor.repository;

import com.ai.api.professor.domain.Professor;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfessorRepository extends JpaRepository<Professor, Long> {
    Page<Professor> findAll(Pageable pageable);

    Optional<Professor> findById(Long Id);

    Page<Professor> findByProfessorNameContaining(String name, Pageable pageable);
    Page<Professor> findByDepartment(String Department, Pageable pageable);

}
