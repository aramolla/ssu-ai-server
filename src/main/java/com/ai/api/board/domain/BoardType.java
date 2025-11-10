package com.ai.api.board.domain;

public enum BoardType {
    LIST("목록형"),
    THUMBNAIL("썸네일형"),
    FAQ("FAQ"),
    CALENDER("캘린더형"),
    COMPANY("회사형");

    private final String description;

    BoardType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
