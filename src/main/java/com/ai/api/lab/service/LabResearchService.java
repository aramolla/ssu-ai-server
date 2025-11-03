package com.ai.api.lab.service;

import com.ai.api.lab.domain.LabResearch;
import com.ai.api.lab.dto.LabResearchReqDTO;
import com.ai.api.lab.repository.LabResearchRepoository;
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

    @Transactional(readOnly = true)
    public List<LabResearch> getAllResearch(Pageable pageable) {
        List<LabResearch> LabResearchList = labResearchRepoository.findAll(pageable).getContent();
        return LabResearchList;
    }

    @Transactional(readOnly = true)
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

        if(labResearchReqDTO.getThumbnail() != null){
            Attachment thumbnail = attachmentService.saveAttachment(labResearchReqDTO.getThumbnail());
            labResearch.setThumbnail(thumbnail);
        }

        LabResearch savedLabResearch = labResearchRepoository.save(labResearch);

        if(labResearchReqDTO.getAttachments()!=null){
            attachmentService.savePostAttachments(labResearchReqDTO.getAttachments(), savedLabResearch);
        }

        log.info("연구활동 게시글 생성 완료: {}", savedLabResearch.getId());

        return savedLabResearch;
    }

    public LabResearch updateLabResearch(Long id, LabResearchReqDTO labResearchReqDTO) {
        LabResearch labResearch = labResearchRepoository.findById(id)
            .orElseThrow(() -> new RuntimeException("LabResearch not found"));

        labResearch.setTitle(labResearchReqDTO.getTitle());
        labResearch.setContent(labResearchReqDTO.getContent());
        labResearch.setNotice(labResearchReqDTO.isNotice());
        labResearch.setLabSiteUrl(labResearchReqDTO.getLabSiteUrl());

        if(labResearchReqDTO.getThumbnail() != null){
            if(labResearch.getThumbnail() != null){
                attachmentService.deleteAttachment(labResearch.getThumbnail().getId());
            }
            Attachment thumbnail = attachmentService.saveAttachment(labResearchReqDTO.getThumbnail());
            labResearch.setThumbnail(thumbnail);
        }

        LabResearch updatedLabResearch = labResearchRepoository.save(labResearch);

        if(labResearchReqDTO.getAttachments()!=null){
            attachmentService.deletePostAttachments(labResearch);
            attachmentService.savePostAttachments(labResearchReqDTO.getAttachments(), updatedLabResearch);
        }

        return updatedLabResearch;

    }


    public LabResearch deleteLabResearch(Long id) {
        LabResearch labResearch = labResearchRepoository.findById(id)
            .orElseThrow(() -> new RuntimeException("LabResearch not found"));

        if(labResearch.getThumbnail()!=null){
            attachmentService.deleteAttachment(labResearch.getThumbnail().getId());
        }
        if(labResearch.getPostAttachments()!=null){
            attachmentService.deletePostAttachments(labResearch);
        }

       labResearchRepoository.deleteById(id);

        return labResearch;
    }







}
