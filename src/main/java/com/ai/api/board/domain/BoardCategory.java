package com.ai.api.board.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum BoardCategory {
    NOTICE("공지사항", 1L, "notice"),
    BUDGET("예결산 공고", 2L, "budget"),
    DONATION("기부금", 3L, "donation"),
    SAFETY("안전·보건", 4L, "safety"),
    RECRUITMENT("취업정보", 5L, "recruitment"),
    ETC("기타", 6L, "etc"),
    DATAROOM("자료실", 7L, "dataroom"),
    MENUAL("규정 및 메뉴얼", 8L, "menual"),
    FAQ("FAQ", 9L, "faq"),
    RESEARCH("연구성과", 10L, "research"),
    PROFESSOR("교수진", 11L, "professor"),
    CALENDER("학사일정", 12L, "calender"),
    GATHERING("소모임", 13L, "gathering"),
    PRECAUTIONS("학부소식", 14L, "precautions"),
    LAB_RESEARCH("연구활동 소개", 15L, "lab-research");

    private final String title;
    private final Long id;
    private final String enName;

    public static BoardCategory fromEnName(String enName) {
        for (BoardCategory category : values()) {
            if (category.getEnName().equals(enName)) {
                return category;
            }
        }
        throw new IllegalArgumentException("존재하지 않는 게시판입니다: " + enName);
    }
}
