package com.ai.api.board.recruitment.controller;

import com.ai.api.board.domain.Recruitment;
import com.ai.api.board.recruitment.dto.RecruitmentReqDTO;
import com.ai.api.board.recruitment.dto.RecruitmentResDTO;
import com.ai.api.board.recruitment.service.RecruitmentService;
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
@RequestMapping("/recruitment")
@Slf4j
@RequiredArgsConstructor
public class RecruitmentController {

    private final RecruitmentService recruitmentService;

    @GetMapping
    public ResponseEntity<List<RecruitmentResDTO>> getAllRecruitments(
        @PageableDefault(sort = "id") Pageable pageable
    ) {
        log.info("취업정보 게시판 목록 조회");
        List<RecruitmentResDTO> recruitmentList = recruitmentService.getAllRecruitments(pageable)
            .stream()
            .map(RecruitmentResDTO::from)
            .collect(Collectors.toList());

        return ResponseEntity.ok(recruitmentList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RecruitmentResDTO> getDetailRecruitment(
        @PathVariable Long id
    ){
        log.info("취업정보 상세 조회: {}", id);
        Recruitment detailRecruitment = recruitmentService.getDetailRecruitment(id);
        return ResponseEntity.ok(RecruitmentResDTO.from(detailRecruitment));
    }


    @GetMapping("/search")
    public ResponseEntity<List<RecruitmentResDTO>> searchRecruitment(
        @Param(value = "keyword") String keyword,
        Pageable pageable
    ) {
        log.info("취업정보 검색 내용 조회");
        List<RecruitmentResDTO> searchRecruitment = recruitmentService.searchRecruitment(keyword, pageable)
            .stream()
            .map(RecruitmentResDTO::from)
            .collect(Collectors.toList());

        return ResponseEntity.ok(searchRecruitment);
    }

    @PostMapping
    public ResponseEntity<RecruitmentResDTO> saveRecruitment(
        @RequestPart("recruitmentReqDTO") RecruitmentReqDTO recruitmentReqDTO,
        @RequestPart("attachment") List<MultipartFile> attachment
    ) {
        log.info("취업정보 게시글 저장: {}", recruitmentReqDTO);
        recruitmentReqDTO.setAttachments(attachment);
        Recruitment saveRecruitment = recruitmentService.saveRecruitment(recruitmentReqDTO);

        return ResponseEntity.ok(RecruitmentResDTO.from(saveRecruitment));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RecruitmentResDTO> updateRecruitment(
        @PathVariable Long id,
        @RequestPart("recruitmentReqDTO") RecruitmentReqDTO recruitmentReqDTO,
        @RequestPart("attachment") List<MultipartFile> attachment
    ) {
        log.info("취업정보 게시글 수정: {}", id);
        recruitmentReqDTO.setAttachments(attachment);
        Recruitment updateRecruitment = recruitmentService.updateRecruitment(id, recruitmentReqDTO);

        return ResponseEntity.ok(RecruitmentResDTO.from(updateRecruitment));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<RecruitmentResDTO> deleteRecruitment(
        @PathVariable Long id
    ) {
        log.info("취업정보 게시글 삭제: {}", id);
        recruitmentService.deleteRecruitment(id);

        return ResponseEntity.noContent().build();
    }





}
