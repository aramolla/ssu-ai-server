package com.ai.api.professor.controller;

import com.ai.api.professor.domain.Professor;
import com.ai.api.professor.dto.ProfessorReqDTO;
import com.ai.api.professor.dto.ProfessorResDTO;
import com.ai.api.professor.service.ProfessorService;
import java.util.stream.Collectors;
import org.springframework.data.domain.Pageable;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/professor")
public class ProfessorController {

    private final ProfessorService professorService;

    // TODO: 썸네일 추가 로직 필요

    @GetMapping
    public ResponseEntity<List<ProfessorResDTO>> getAllProfessor(@PageableDefault(sort = "id") Pageable pageable) {
        log.info("교수 목록 조회");
        List<ProfessorResDTO> professor = professorService.getAllProfessors(pageable)
            .stream()
            .map(ProfessorResDTO::from)
            .collect(Collectors.toList());
        return ResponseEntity.ok(professor);
    }

    @PostMapping
    public ResponseEntity<ProfessorResDTO> saveProfessor(@RequestBody ProfessorReqDTO professorReq) {
        log.info("교수 정보 등록");
        Professor professor = professorService.addProfessor(professorReq);
        return ResponseEntity.ok(ProfessorResDTO.from(professor));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProfessorResDTO> updateProfessor(@PathVariable("id") Long id, @RequestBody ProfessorReqDTO professorReq){
        log.info("교수 정보 수정");
        Professor professor = professorService.updateProfessor(id, professorReq);
        return ResponseEntity.ok(ProfessorResDTO.from(professor));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProfessor(@PathVariable("id") Long id){
        log.info("교수 정보 삭제");
        professorService.deleteProfessor(id);
        return ResponseEntity.ok().build();
    }




}
