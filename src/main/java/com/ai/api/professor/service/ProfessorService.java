package com.ai.api.professor.service;

import com.ai.api.professor.domain.Professor;
import com.ai.api.professor.domain.ProfessorHistory;
import com.ai.api.professor.domain.ProfessorImage;
import com.ai.api.professor.dto.HistoryDTO;
import com.ai.api.professor.dto.ProfessorReqDTO;
import com.ai.api.professor.repository.ProfessorRepository;
import com.ai.api.resource.service.FileService;
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
    private final FileService fileService;

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
            .professorName(professorReq.getProfessorName())
            .professorEmail(professorReq.getProfessorEmail())
            .department(professorReq.getDepartment())
            .major(professorReq.getMajor())
            .office(professorReq.getOffice())
            .tel(professorReq.getTel())
            .build();

        if(professorReq.getHistorise() != null){
            for (HistoryDTO historyDTO : professorReq.getHistorise()) {
                ProfessorHistory history = historyDTO.toEntity();
                professor.addHistory(history);
            }
        }

        if(professorReq.getImage() != null){
            ProfessorImage saveImage = fileService.saveFile(professorReq.getImage());
            professor.setImage(saveImage);
            saveImage.setProfessor(professor);
        }

        Professor savePro = professorRepository.save(professor);
        log.info("교수 정보 등록 완료: {}", savePro);

        return savePro;
    }


    public Professor updateProfessor(Long id, ProfessorReqDTO professorReq) {
        Professor existingProfessor = professorRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Professor not found"));

        existingProfessor.setProfessorName(professorReq.getProfessorName());
        existingProfessor.setProfessorEmail(professorReq.getProfessorEmail());
        existingProfessor.setDepartment(professorReq.getDepartment());
        existingProfessor.setMajor(professorReq.getMajor());
        existingProfessor.setOffice(professorReq.getOffice());
        existingProfessor.setTel(professorReq.getTel());

        existingProfessor.clearHistories();
        if (professorReq.getHistorise() != null) {
            for (HistoryDTO historyDTO : professorReq.getHistorise()) {
                ProfessorHistory history = historyDTO.toEntity();
                existingProfessor.addHistory(history);
            }
        }

        if(professorReq.getImage() != null){
            existingProfessor.setImage(fileService.saveFile(professorReq.getImage()));
            log.info("새 이미지로 덮어씌우기");
        }

        return professorRepository.save(existingProfessor);
    }

    public void deleteProfessor(Long id) {
        Professor delProfessor = professorRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Professor not found"));

        if(delProfessor.getImage() != null){
            fileService.deleteImage(id);
        }
        professorRepository.deleteById(id);
        log.info("교수 정보 삭제 완료: {}", delProfessor.getProfessorName());
    }




}
