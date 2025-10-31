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
import org.springframework.web.multipart.MultipartFile;

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

    private MultipartFile image;

    private String office;
    private String tel;


}
