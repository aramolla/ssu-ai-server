package com.ai.api.professor.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "professor_histories")
@NoArgsConstructor
@Data
public class ProfessorHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "professor_id") // 외래키, ProfessorHistory가 주인 테이블
    @ToString.Exclude
    private Professor professor;

    // 기관명
    @Column(nullable = true)
    private String institution;
    // 부서
    @Column(nullable = true)
    private String department;
    // 직책
    @Column(nullable = true)
    private String position;



}
