package com.ai.api.board.lab.service;

import com.ai.api.board.domain.LabResearch;
import com.ai.api.board.domain.Notice;
import com.ai.api.board.lab.dto.LabResearchReqDTO;
import com.ai.api.board.lab.repository.LabResearchRepoository;
import com.ai.api.resource.domain.Attachment;
import com.ai.api.resource.service.AttachmentService;
import org.springframework.data.domain.Pageable;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class LabResearchService {

    private final LabResearchRepoository labResearchRepoository;
    private final AttachmentService attachmentService;

    public List<LabResearch> getAllResearch(Pageable pageable) {
        List<LabResearch> LabResearchList = labResearchRepoository.findAll(pageable).getContent();
        return LabResearchList;
    }

    public LabResearch getDetailLabResearch(Long id) {
        labResearchRepoository.incrementViewCount(id); // 중복 조회 여부 무시

        return labResearchRepoository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Invalid LabResearch"));
    }

    public List<LabResearch> searchLabResearch(String keyword, Pageable pageable) {
        List<LabResearch> searchList = labResearchRepoository.searchByTitle(keyword, pageable).getContent();
        return searchList;
    }

    public LabResearch saveLabResearch(LabResearchReqDTO labResearchReqDTO) {

        LabResearch labResearch = LabResearch.builder()
            .title(labResearchReqDTO.getTitle())
            .content(labResearchReqDTO.getContent())
            .isNotice(labResearchReqDTO.isNotice())
            .labSiteUrl(labResearchReqDTO.getLabSiteUrl())
            .build();

        if(labResearchReqDTO.getThumbnail() != null && !labResearchReqDTO.getThumbnail().isEmpty()){
            Attachment thumbnail = attachmentService.saveAttachment(labResearchReqDTO.getThumbnail());
            labResearch.setThumbnail(thumbnail);
        }

        LabResearch savedLabResearch = labResearchRepoository.save(labResearch);

        if(labResearchReqDTO.getAttachments()!=null){
            attachmentService.savePostAttachments(labResearchReqDTO.getThumbnail(), labResearchReqDTO.getAttachments(), savedLabResearch);
        }

        log.info("연구활동 게시글 생성 완료: {}", savedLabResearch.getId());

        return savedLabResearch;
    }

    public LabResearch updateLabResearch(Long id, LabResearchReqDTO labResearchReqDTO) {
        LabResearch labResearch = labResearchRepoository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("LabResearch not found"));

        labResearch.setTitle(labResearchReqDTO.getTitle());
        labResearch.setContent(labResearchReqDTO.getContent());
        labResearch.setNotice(labResearchReqDTO.isNotice());
        labResearch.setLabSiteUrl(labResearchReqDTO.getLabSiteUrl());

        if(labResearchReqDTO.getThumbnail() != null && !labResearchReqDTO.getThumbnail().isEmpty()){
            if(labResearch.getThumbnail() != null){
                attachmentService.deleteAttachment(labResearch.getThumbnail().getId());
            }
            Attachment thumbnail = attachmentService.saveAttachment(labResearchReqDTO.getThumbnail());
            labResearch.setThumbnail(thumbnail);
        }

        LabResearch updatedLabResearch = labResearchRepoository.save(labResearch);

        if(labResearchReqDTO.getAttachments()!=null){
            attachmentService.deletePostAttachments(labResearch);
            attachmentService.savePostAttachments(labResearchReqDTO.getThumbnail(), labResearchReqDTO.getAttachments(), updatedLabResearch);
        }

        return updatedLabResearch;
    }


    public void deleteLabResearch(Long id) {
        LabResearch labResearch = labResearchRepoository.findById(id)
            .orElseThrow(() -> new RuntimeException("LabResearch not found"));

        if(labResearch.getThumbnail()!=null){
            attachmentService.deleteAttachment(labResearch.getThumbnail().getId());
        }
        if(labResearch.getPostAttachments()!=null){
            attachmentService.deletePostAttachments(labResearch);
        }

       labResearchRepoository.deleteById(id);
    }







}
