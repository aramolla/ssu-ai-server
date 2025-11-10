package com.ai.common.exception;

public class InvalidTokenException extends RuntimeException { // 유효하지 않은 토큰 (401)
    public InvalidTokenException(String message) {
        super(message);
    }
}