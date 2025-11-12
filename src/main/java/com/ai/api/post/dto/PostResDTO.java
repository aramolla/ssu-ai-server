package com.ai.api.post.dto;

import com.ai.api.board.domain.Board;
import com.ai.api.board.domain.BoardType;
import com.ai.api.post.domain.Post;
import com.ai.api.resource.domain.Attachment;
import com.ai.api.resource.dto.AttachmentDTO;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostResDTO {
    private Long boardId;
    private String boardEnName;
    private String boardTitle;
    private BoardType boardType;
    // Posts 공통 필드
    private Long id;
    private String title;
    private String content;
    private boolean isNotice;
    private Integer viewCount;
    private LocalDateTime createdAt;

    // 동적 필드 정보
    private DynamicField sub1;
    private DynamicField sub2;
    private DynamicField sub3;
    private DynamicField sub4;
    private DynamicField sub5;

    private List<AttachmentDTO> attachments;
    // thumbnail
    private String thumbnailUrl;
    // calendar
    private LocalDate startedAt;
    private LocalDate endedAt;
    // 카테고리
    private String category;
    private String[] availableCategories;  // Board에 설정된 카테고리 목록

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DynamicField {
        private String label;   // Board의 subXLabel
        private String value;   // Post의 subXValue
        private boolean hasValue;  // label이 null이 아닌지 여부
    }

    public static PostResDTO from(Post post) {
        Board board = post.getBoard();

        List<AttachmentDTO> attachmentDTOS = Optional.ofNullable(post.getPostAttachments())
            .orElseGet(ArrayList::new) // null일 경우 빈 리스트 반환
            .stream()
            .map(pa -> AttachmentDTO.from(pa.getAttachment()))
            .collect(Collectors.toList());

        String thumbnailUrl = Optional.ofNullable(post.getThumbnail())
            .map(Attachment::getFilePath)
            .orElse(null);


        return PostResDTO.builder()
            .id(post.getId())
            .boardId(board.getId())
            .boardEnName(board.getBoardEnName())
            .boardTitle(board.getTitle())
            .boardType(post.getBoard().getBoardType())
            .title(post.getTitle())
            .content(post.getContent())
            .isNotice(post.isNotice())
            .viewCount(post.getView_count())
            .createdAt(post.getCreatedAt())
            .sub1(buildDynamicField(board.getSub1Label(), post.getSub1Value()))
            .sub2(buildDynamicField(board.getSub2Label(), post.getSub2Value()))
            .sub3(buildDynamicField(board.getSub3Label(), post.getSub3Value()))
            .sub4(buildDynamicField(board.getSub4Label(), post.getSub4Value()))
            .sub5(buildDynamicField(board.getSub5Label(), post.getSub5Value()))
            .attachments(attachmentDTOS)
            .category(post.getCategory())
            .thumbnailUrl(thumbnailUrl)
            .startedAt(post.getStartedAt())
            .endedAt(post.getEndedAt())
            .availableCategories(parseCategories(board.getCategories()))
            .build();
    }

    private static DynamicField buildDynamicField(String label, String value) {
        return DynamicField.builder()
            .label(label)
            .value(value)
            .hasValue(label != null && !label.isEmpty())
            .build();
    }

    private static String[] parseCategories(String categoriesJson) {
        if (categoriesJson == null || categoriesJson.isEmpty()) {
            return new String[0];
        }
        // JSON 배열 파싱
        return categoriesJson
            .replace("[", "")
            .replace("]", "")
            .replace("\"", "")
            .split(",");
    }


}
