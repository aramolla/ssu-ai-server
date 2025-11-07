package com.ai.api.board.research.controller;

import com.ai.api.board.domain.Research;
import com.ai.api.board.research.dto.ResearchReqDTO;
import com.ai.api.board.research.dto.ResearchResDTO;
import com.ai.api.board.research.service.ResearchService;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/research")
@Slf4j
@RequiredArgsConstructor
public class ResearchController {

    private final ResearchService researchService;

    @GetMapping
    public ResponseEntity<List<ResearchResDTO>> getAllResearchs(
        @PageableDefault(sort = "id") Pageable pageable
    ) {
        log.info("연구성과 게시판 목록 조회");
        List<ResearchResDTO> researchList = researchService.getAllResearchs(pageable)
            .stream()
            .map(ResearchResDTO::from)
            .collect(Collectors.toList());

        return ResponseEntity.ok(researchList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResearchResDTO> getDetailResearch(
        @PathVariable Long id
    ){
        log.info("연구성과 상세 조회: {}", id);
        Research detailResearch = researchService.getDetailResearch(id);
        return ResponseEntity.ok(ResearchResDTO.from(detailResearch));
    }

    @GetMapping("/search")
    public ResponseEntity<List<ResearchResDTO>> searchResearch(
        @Param(value = "keyword") String keyword,
        Pageable pageable
    ){
        log.info("연구성과 검색 내용 조회");
        List<ResearchResDTO> searchResearchList = researchService.searchResearch(keyword, pageable)
            .stream()
            .map(ResearchResDTO::from)
            .collect(Collectors.toList());

        return ResponseEntity.ok(searchResearchList);
    }

    @PostMapping
    public ResponseEntity<ResearchResDTO> saveResearch(
        @RequestPart("researchReqDTO") ResearchReqDTO researchReqDTO,
        @RequestPart("attachment") List<MultipartFile> attachment
    ){
        log.info("연구성과 게시글 저장: {}", researchReqDTO);
        researchReqDTO.setAttachments(attachment);
        Research saveResearch = researchService.saveResearch(researchReqDTO);

        return ResponseEntity.ok(ResearchResDTO.from(saveResearch));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResearchResDTO> updateResearch(
        @PathVariable Long id,
        @RequestPart("researchReqDTO") ResearchReqDTO researchReqDTO,
        @RequestPart("attachment") List<MultipartFile> attachment
    ){
        log.info("연구성과 게시글 수정: {}", id);
        researchReqDTO.setAttachments(attachment);
        Research updateResearch = researchService.updateResearch(id, researchReqDTO);

        return ResponseEntity.ok(ResearchResDTO.from(updateResearch));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResearchResDTO> deleteResearch(
        @PathVariable Long id
    ){
        log.info("연구성과 게시글 삭제: {}", id);
        researchService.deleteResearch(id);

        return ResponseEntity.noContent().build();
    }

}
