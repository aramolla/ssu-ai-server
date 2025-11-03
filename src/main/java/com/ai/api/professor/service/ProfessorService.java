package com.ai.api.professor.service;

import com.ai.api.professor.domain.Professor;
import com.ai.api.professor.dto.ProfessorReqDTO;
import com.ai.api.professor.repository.ProfessorRepository;
import com.ai.api.resource.domain.Attachment;
import com.ai.api.resource.service.AttachmentService;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class ProfessorService {

    private final ProfessorRepository professorRepository;
    private final AttachmentService attachmentService;

    public List<Professor> getAllProfessors(Pageable pageable) {
        List<Professor> professors = professorRepository.findAll(pageable).getContent();
        return professors;
    }

    public List<Professor> searchProfessor(String keyword, Pageable pageable) {
        List<Professor> searchProfessor = professorRepository.searchByKeyword(keyword, pageable).getContent();
        return searchProfessor;
    }

    public Professor addProfessor(ProfessorReqDTO professorReq) {
        Professor professor = Professor.builder()
            .title(professorReq.getProfessorName())
            .professorEmail(professorReq.getProfessorEmail())
            .content(professorReq.getDepartment())
            .major(professorReq.getMajor())
            .historise(professorReq.getHistorise())
            .office(professorReq.getOffice())
            .tel(professorReq.getTel())
            .build();


        if(professorReq.getImage() != null){
            Attachment saveImage = attachmentService.saveAttachment(professorReq.getImage());
            professor.setImage(saveImage);
        }

        Professor savePro = professorRepository.save(professor);
        log.info("교수 정보 등록 완료: {}", savePro);

        return savePro;
    }


    public Professor updateProfessor(Long id, ProfessorReqDTO professorReq) {
        Professor existingProfessor = professorRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Professor not found"));

        existingProfessor.setTitle(professorReq.getProfessorName());
        existingProfessor.setProfessorEmail(professorReq.getProfessorEmail());
        existingProfessor.setContent(professorReq.getDepartment());
        existingProfessor.setMajor(professorReq.getMajor());
        existingProfessor.setHistorise(professorReq.getHistorise());
        existingProfessor.setOffice(professorReq.getOffice());
        existingProfessor.setTel(professorReq.getTel());


        if(professorReq.getImage() != null){
            existingProfessor.setImage(attachmentService.saveAttachment(professorReq.getImage()));
            log.info("새 이미지로 덮어씌우기");
        }

        return professorRepository.save(existingProfessor);
    }

    public void deleteProfessor(Long id) {
        Professor delProfessor = professorRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Professor not found"));

        if(delProfessor.getImage() != null){
            attachmentService.deleteAttachment(delProfessor.getImage().getId());
        }
        professorRepository.deleteById(id);
        log.info("교수 정보 삭제 완료: {}", delProfessor.getTitle());
    }




}
