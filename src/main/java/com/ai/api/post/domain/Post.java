package com.ai.api.post.domain;

import com.ai.api.board.domain.Board;
import com.ai.api.resource.domain.Attachment;
import com.ai.api.resource.domain.PostAttachment;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "post")
@Inheritance(strategy = InheritanceType.JOINED)
@ToString(callSuper = true, exclude = {"board", "postAttachments"})
@Data
@SuperBuilder
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id", nullable = false)
    private Board board;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    private boolean isNotice = false;

    private Integer view_count = 0;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PostAttachment> postAttachments;

    // 동적 필드
    @Column(length = 255)
    private String sub1Value;
    @Column(length = 255)
    private String sub2Value;
    @Column(length = 255)
    private String sub3Value;
    @Column(length = 255)
    private String sub4Value;
    @Column(length = 255)
    private String sub5Value;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "thumbnail_id")
    private Attachment thumbnail;

    @Column(length = 100)
    private String category;




}