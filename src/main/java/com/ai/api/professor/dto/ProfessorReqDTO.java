package com.ai.api.professor.dto;

import com.ai.api.professor.domain.Professor;
import com.ai.api.professor.domain.ProfessorHistory;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProfessorReqDTO {
    @NotBlank(message = "이름을 입력해주세요")
    private String professorName;
    @NotBlank(message = "메일을 입력해주세요")
    private String professorEmail;
    @NotBlank(message = "학과를 입력해주세요")
    private String department;
    @NotBlank(message = "연구분야를 입력해주세요")
    private String major;

    @Valid
    private List<HistoryDTO> historise = new ArrayList<>();

    private String office;
    private String tel;

    public Professor toEntity(){
        Professor professor = new Professor();
        professor.setProfessorName(this.professorName);
        professor.setProfessorEmail(this.professorEmail);
        professor.setDepartment(this.department);
        professor.setMajor(this.major);
        professor.setOffice(this.office);
        professor.setTel(this.tel);

        if(this.historise != null){
            List<ProfessorHistory> professorHistories = new ArrayList<>();
            for(HistoryDTO historyDTO : this.historise){
                professorHistories.add(historyDTO.toEntity());
            }
            professor.setHistorise(professorHistories);
        }

        return professor;
    }

}
