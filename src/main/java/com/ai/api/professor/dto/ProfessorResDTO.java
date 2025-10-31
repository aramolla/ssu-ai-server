package com.ai.api.professor.dto;

import com.ai.api.professor.domain.Professor;
import java.time.LocalDateTime;
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
public class ProfessorResDTO {
    private Long id;
    private String professorName;
    private String professorEmail;
    private String department;
    private String major;
    private String office;
    private String tel;
    private String image;

    private List<HistoryDTO> historise = new ArrayList<>();

    private LocalDateTime createdAt;

    public static ProfessorResDTO from(Professor professor) {

        List<HistoryDTO> historise = new ArrayList<>();
        if (professor.getHistorise()!=null){
            historise = professor.getHistorise().stream()
                .map(HistoryDTO::from)
                .collect(Collectors.toList());
        }

        String existImage = null;
        if (professor.getImage()!=null){
            existImage = professor.getImage().getImagePath();
        }


        return ProfessorResDTO.builder()
            .id(professor.getId())
            .professorName(professor.getProfessorName())
            .professorEmail(professor.getProfessorEmail())
            .department(professor.getDepartment())
            .major(professor.getMajor())
            .office(professor.getOffice())
            .tel(professor.getTel())
            .image(existImage)
            .historise(historise)
            .createdAt(professor.getCreateTime())
            .build();
    }

}
