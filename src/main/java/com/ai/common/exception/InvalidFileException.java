package com.ai.common.exception;

public class InvalidFileException extends RuntimeException { // 잘못된 파일을 업로드했을 때 (400)
    public InvalidFileException(String message) {
        super(message);
    }
}