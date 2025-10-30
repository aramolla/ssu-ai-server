package com.ai.api.professor.dto;

import com.ai.api.professor.domain.ProfessorHistory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HistoryDTO {

    private Long id;

    private String institution;
    private String department;
    private String position;

    public static HistoryDTO from(ProfessorHistory history){ // Entity -> DTO
        return HistoryDTO.builder()
            .id(history.getId())
            .institution(history.getInstitution())
            .department(history.getDepartment())
            .position(history.getPosition())
            .build();
    }

    public ProfessorHistory toEntity() {
        ProfessorHistory history = new ProfessorHistory();
        history.setInstitution(this.institution);
        history.setDepartment(this.department);
        history.setPosition(this.position);
        return history;
    }

}
