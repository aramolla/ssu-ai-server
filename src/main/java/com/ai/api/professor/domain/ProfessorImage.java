package com.ai.api.professor.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name = "professor_images")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProfessorImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String originalImageName;

    @Column(nullable = false)
    private String storedImageName;

    @Column(nullable = false)
    private String imagePath;

    @Column(nullable = false)
    private Long imageSize;

    @OneToOne
    @JoinColumn(name = "professor_id", unique = true) // 외래키, ProfessorImage가 주인 테이블
    @ToString.Exclude
    private Professor professor;

    @Column(nullable = false)
    private String contentType;

    @CreationTimestamp
    private LocalDateTime createTime;

}
