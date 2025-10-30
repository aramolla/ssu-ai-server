package com.ai.api.professor.domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "professors")
@Data
@Builder
public class Professor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Long id;

    @Column(nullable = false)
    private String professorName;

    @Email
    @Column(unique = true, nullable = false)
    private String professorEmail;

    @Column(nullable = false)
    private String department; // 부서

    @Column(nullable = false)
    private String major; // 연구 분야

    @OneToMany(mappedBy = "professor", cascade = CascadeType.ALL, orphanRemoval = true)
//    @Builder.Default
    private List<ProfessorHistory> historise = new ArrayList<>(); //경력

    @Column(nullable = false)
    private String office;

    @Column(nullable = false)
    private String tel;

    private String thumnailPath;

    @CreationTimestamp
    private LocalDateTime createTime;

    public void addHistory(ProfessorHistory history) {
        historise.add(history);
        history.setProfessor(this);
    }

    public void removeHistory(ProfessorHistory history) {
        historise.remove(history);
        history.setProfessor(null);
    }

    public void clearHistories() {
        historise.forEach(history -> history.setProfessor(null));
        historise.clear();
    }



}
