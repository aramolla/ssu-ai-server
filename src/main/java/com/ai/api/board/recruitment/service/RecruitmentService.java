package com.ai.api.board.recruitment.service;

import com.ai.api.board.domain.Recruitment;
import com.ai.api.board.recruitment.dto.RecruitmentReqDTO;
import com.ai.api.board.recruitment.repository.RecruitmentRepository;
import com.ai.api.resource.service.AttachmentService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class RecruitmentService {

    private final RecruitmentRepository recruitmentRepository;
    private final AttachmentService attachmentService;

    public List<Recruitment> getAllRecruitments(Pageable pageable) {
        return recruitmentRepository.findAll(pageable).getContent();
    }

    public Recruitment getDetailRecruitment(Long id) {
        recruitmentRepository.incrementViewCount(id);

        return recruitmentRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Recruitment not found"));
    }

    public List<Recruitment> searchRecruitment(String keyword, Pageable pageable) {
        return recruitmentRepository.searchByTitle(keyword, pageable).getContent();
    }

    public Recruitment saveRecruitment(RecruitmentReqDTO recruitmentReqDTO) {
        Recruitment recruitment = Recruitment.builder()
            .title(recruitmentReqDTO.getTitle())
            .content(recruitmentReqDTO.getContent())
            .isNotice(recruitmentReqDTO.isNotice())
            .view_count(0)
            .build();
        Recruitment savedRecruitment = recruitmentRepository.save(recruitment);

        if(recruitmentReqDTO.getAttachments() != null) {
            attachmentService.savePostAttachments(null, recruitmentReqDTO.getAttachments(), savedRecruitment);
        }

        return savedRecruitment;
    }

    public Recruitment updateRecruitment(Long id, RecruitmentReqDTO recruitmentReqDTO) {
        Recruitment recruitment = recruitmentRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Recruitment not found"));

        recruitment.setTitle(recruitmentReqDTO.getTitle());
        recruitment.setContent(recruitmentReqDTO.getContent());
        recruitment.setNotice(recruitmentReqDTO.isNotice());

        if(recruitmentReqDTO.getAttachments() != null) {
            attachmentService.deletePostAttachments(recruitment);
            attachmentService.savePostAttachments(null, recruitmentReqDTO.getAttachments(), recruitment);
        }

        return recruitment;
    }

    public void deleteRecruitment(Long id) {
        Recruitment recruitment = recruitmentRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Recruitment not found"));

        if(recruitment.getPostAttachments()!=null){
            attachmentService.deletePostAttachments(recruitment);
        }

        recruitmentRepository.delete(recruitment);
    }

}
