package com.ai.api.board.research.service;

import com.ai.api.board.domain.Research;
import com.ai.api.board.research.dto.ResearchReqDTO;
import com.ai.api.board.research.repository.ResearchRepository;
import com.ai.api.resource.service.AttachmentService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ResearchService {

    private final ResearchRepository researchRepository;
    private final AttachmentService attachmentService;

    public List<Research> getAllResearchs(Pageable pageable) {
        return researchRepository.findAll(pageable).getContent();
    }

    public Research getDetailResearch(Long id) {
        researchRepository.incrementViewCount(id);

        return researchRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Research not found"));
    }

    public List<Research> searchResearch(String keyword, Pageable pageable) {
        return researchRepository.searchByTitle(keyword, pageable).getContent();
    }

    public Research saveResearch(ResearchReqDTO researchReqDTO) {
        Research research = Research.builder()
            .title(researchReqDTO.getTitle())
            .content(researchReqDTO.getContent())
            .isNotice(researchReqDTO.isNotice())
            .build();

        Research savedResearch = researchRepository.save(research);

        if(researchReqDTO.getAttachments() != null){
            attachmentService.savePostAttachments(null, researchReqDTO.getAttachments(), savedResearch);
        }

        return savedResearch;

    }

    public Research updateResearch(Long id, ResearchReqDTO researchReqDTO) {
        Research updateResearch = researchRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("research not found"));

        updateResearch.setTitle(researchReqDTO.getTitle());
        updateResearch.setContent(researchReqDTO.getContent());
        updateResearch.setNotice(researchReqDTO.isNotice());

        if(researchReqDTO.getAttachments() != null){
            attachmentService.deletePostAttachments(updateResearch);
            attachmentService.savePostAttachments(null, researchReqDTO.getAttachments(), updateResearch);
        }

        return updateResearch;
    }

    public void deleteResearch(Long id) {
        Research research = researchRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("research not found"));

        if(research.getPostAttachments()!=null){
            attachmentService.deletePostAttachments(research);
        }

        researchRepository.deleteById(id);
    }





}
