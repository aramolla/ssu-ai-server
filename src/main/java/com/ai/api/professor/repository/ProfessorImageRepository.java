package com.ai.api.professor.repository;

import com.ai.api.professor.domain.ProfessorImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfessorImageRepository extends JpaRepository<ProfessorImage, Long> {

}
