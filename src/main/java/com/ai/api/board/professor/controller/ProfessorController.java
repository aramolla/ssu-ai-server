package com.ai.api.board.professor.controller;

import com.ai.api.board.domain.LabResearch;
import com.ai.api.board.domain.Professor;
import com.ai.api.board.lab.dto.LabResearchResDTO;
import com.ai.api.board.professor.dto.ProfessorReqDTO;
import com.ai.api.board.professor.dto.ProfessorResDTO;
import com.ai.api.board.professor.service.ProfessorService;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/professors")
public class ProfessorController {

    private final ProfessorService professorService;

    @GetMapping
    public ResponseEntity<List<ProfessorResDTO>> getAllProfessor(
        @PageableDefault(sort = "id", size = 9) Pageable pageable
    ) {
        log.info("교수 목록 조회");
        List<ProfessorResDTO> professor = professorService.getAllProfessors(pageable)
            .stream()
            .map(ProfessorResDTO::from)
            .collect(Collectors.toList());
        return ResponseEntity.ok(professor);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProfessorResDTO> getDetailProfessor(
        @PathVariable Long id
    ){
        log.info("교수 소개 게시글 {} 상세 조회", id);
        Professor detailProfessor = professorService.getDetailProfessor(id);
        return ResponseEntity.ok(ProfessorResDTO.from(detailProfessor));
    }


    @GetMapping("/search")
    public ResponseEntity<List<ProfessorResDTO>> searchProfessor(
        @RequestParam(value = "keyword") String keyword,
        @PageableDefault(sort = "id", size = 9) Pageable pageable
    ){
        log.info("키워드 검색");
        List<ProfessorResDTO> professorKeyword = professorService.searchProfessors(keyword, pageable)
            .stream()
            .map(ProfessorResDTO::from)
            .collect(Collectors.toList());
        return ResponseEntity.ok(professorKeyword);
    }


    // -------------- 아래부턴 Admin 전용 기능 --------------

    @PostMapping
    public ResponseEntity<ProfessorResDTO> saveProfessor(
        @RequestPart("professorReqDTO") ProfessorReqDTO professorReq,
        @RequestPart(value = "professorImage", required = false) MultipartFile image
    ) {
        log.info("교수 정보 등록");

        professorReq.setImage(image);
        Professor professor = professorService.saveProfessor(professorReq);
        return ResponseEntity.ok(ProfessorResDTO.from(professor));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProfessorResDTO> updateProfessor(
        @PathVariable("id") Long id,
        @RequestPart("professorReqDTO") ProfessorReqDTO professorReq,
        @RequestPart(value = "professorImage", required = false) MultipartFile image
    ){
        log.info("교수 정보 수정");
        professorReq.setImage(image);
        Professor professor = professorService.updateProfessor(id, professorReq);
        return ResponseEntity.ok(ProfessorResDTO.from(professor));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProfessor(@PathVariable("id") Long id){
        log.info("교수 정보 삭제");
        professorService.deleteProfessor(id);
        return ResponseEntity.noContent().build();
    }




}
