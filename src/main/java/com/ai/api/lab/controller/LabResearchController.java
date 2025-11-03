package com.ai.api.lab.controller;

import com.ai.api.lab.domain.LabResearch;
import com.ai.api.lab.dto.LabResearchReqDTO;
import com.ai.api.lab.dto.LabResearchResDTO;
import com.ai.api.lab.service.LabResearchService;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/lab-research")
public class LabResearchController {

    // TODO: view_count
    private final LabResearchService labResearchService;

    @GetMapping
    public ResponseEntity<List<LabResearchResDTO>> getAllLabResearch(
        @PageableDefault(sort = "id", size = 9) Pageable pageable
    ) {
        log.info("연구 활동 조회");
        List<LabResearchResDTO> LabResearchList = labResearchService.getAllResearch(pageable)
            .stream()
            .map(LabResearchResDTO::from)
            .collect(Collectors.toList());

        return ResponseEntity.ok(LabResearchList);

    }

    @GetMapping("/search")
    public ResponseEntity<List<LabResearchResDTO>> searchLabResearch(
        @Param(value = "keyword")String keyword, Pageable pageable
    ) {
        log.info("검색 내용 조회");
        List<LabResearchResDTO> searchLabResearchList = labResearchService.searchLabResearch(keyword, pageable)
            .stream()
            .map(LabResearchResDTO::from)
            .collect(Collectors.toList());
        return ResponseEntity.ok(searchLabResearchList);
    }


    @PostMapping
    public ResponseEntity<LabResearchResDTO> saveLabResearch(
        @RequestPart("labResearchReqDTO") LabResearchReqDTO labResearchReqDTO,
        @RequestPart("thumbnail") MultipartFile thumbnail,
        @RequestPart("attachment") List<MultipartFile> attachment
    ){
        log.info("연구 활동 등록");
        labResearchReqDTO.setThumbnail(thumbnail);
        labResearchReqDTO.setAttachments(attachment);
        LabResearch saveLabResearch = labResearchService.saveLabResearch(labResearchReqDTO);

        return ResponseEntity.ok(LabResearchResDTO.from(saveLabResearch));
    }

    @PutMapping("/{id}")
    public ResponseEntity<LabResearchResDTO> updateLabResearch(
        @PathVariable("id") Long id,
        @RequestPart("labResearchReqDTO") LabResearchReqDTO labResearchReqDTO,
        @RequestPart("thumbnail") MultipartFile thumbnail,
        @RequestPart("attachment") List<MultipartFile> attachment
    ){
        log.info("연구 활동 수정");
        labResearchReqDTO.setThumbnail(thumbnail);
        labResearchReqDTO.setAttachments(attachment);
        LabResearch updateLabResearch = labResearchService.updateLabResearch(id, labResearchReqDTO);
        return ResponseEntity.ok(LabResearchResDTO.from(updateLabResearch));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<LabResearchResDTO> deleteLabResearch(
        @PathVariable("id") Long id
    ){
        log.info("연구 활동 삭제");
        LabResearch deleteLabResearch = labResearchService.deleteLabResearch(id);
        return ResponseEntity.ok(LabResearchResDTO.from(deleteLabResearch));
    }

}
