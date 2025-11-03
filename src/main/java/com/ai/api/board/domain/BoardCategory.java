package com.ai.api.board.domain;

public enum BoardCategory {
    PROFESSOR("교수진"),
    CALENDAR("학사일정"),
    NOTICE("공지사항"),
    DATAROOM("자료실"),
    RECRUITMENT("채용공고"),
    LAB_RESEARCH("연구활동소개"),
    RESEARCH("연구성과");

    private final String displayName;

    BoardCategory(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
