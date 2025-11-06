package com.ai.api.board.professor.dto;

import com.ai.api.board.domain.Professor;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
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
    private String historise;
    private Integer viewCount;


    private LocalDateTime createdAt;

    public static ProfessorResDTO from(Professor professor) {


        String existImage = null;
        if (professor.getImage()!=null){
            existImage = professor.getImage().getFilePath();
        }


        return ProfessorResDTO.builder()
            .id(professor.getId())
            .professorName(professor.getTitle())
            .professorEmail(professor.getProfessorEmail())
            .department(professor.getContent())
            .major(professor.getMajor())
            .office(professor.getOffice())
            .tel(professor.getTel())
            .image(existImage)
            .historise(professor.getHistorise())
            .viewCount(professor.getView_count())
            .createdAt(professor.getCreatedAt())
            .build();
    }

}
