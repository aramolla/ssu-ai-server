package com.ai.api.professor.service;

import com.ai.api.professor.domain.Professor;
import com.ai.api.professor.domain.ProfessorHistory;
import com.ai.api.professor.dto.HistoryDTO;
import com.ai.api.professor.dto.ProfessorReqDTO;
import com.ai.api.professor.repository.ProfessorRepository;
import java.util.List;
import org.springframework.data.domain.Pageable;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ProfessorService {

    private final ProfessorRepository professorRepository;

    public List<Professor> getAllProfessors(Pageable pageable) {
        List<Professor> professors = professorRepository.findAll(pageable).getContent();
        return professors;
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

        Professor savePro = professorRepository.save(professor);

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

        return professorRepository.save(existingProfessor);
    }

    public void deleteProfessor(Long id) {
        professorRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Professor not found"));
        professorRepository.deleteById(id);
    }




}
