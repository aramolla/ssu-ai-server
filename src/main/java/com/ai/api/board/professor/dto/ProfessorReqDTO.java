package com.ai.api.board.professor.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
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
    private String historise;


    private MultipartFile image;

    private String office;
    private String tel;


}
