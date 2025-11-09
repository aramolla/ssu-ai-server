package com.ai.api.board.domain;

import com.ai.api.post.domain.Post;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "board")
@Data
@SuperBuilder
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Enumerated(EnumType.STRING)
    private BoardType boardType; // LIST, THUMBNAIIL, FAQ, COMPANY

    private Integer pagingNum = 10;

    @Column(columnDefinition = "TEXT")
    private String categories;

    @CreationTimestamp
    private LocalDateTime createdAt;

    // 동적 필드명 설정
    @Column(length = 50)
    private String sub1Label;
    @Column(length = 50)
    private String sub2Label;
    @Column(length = 50)
    private String sub3Label;
    @Column(length = 50)
    private String sub4Label;
    @Column(length = 50)
    private String sub5Label;

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL)
    private List<Post> post;

}
