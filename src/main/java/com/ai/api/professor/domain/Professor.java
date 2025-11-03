package com.ai.api.professor.domain;

import com.ai.api.board.domain.Post;
import com.ai.api.resource.domain.Attachment;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@SuperBuilder
public class Professor extends Post {

    @Email
    @Column(unique = true, nullable = false)
    private String professorEmail;

    @Column(nullable = false)
    private String major; // 연구 분야

    private String historise;

    @Column(nullable = true)
    private String office;

    @Column(nullable = true)
    private String tel;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "thumbnail_id")
    private Attachment image;





}
