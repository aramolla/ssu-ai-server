package com.ai.common.exception;

public class UnauthorizedException extends RuntimeException { // 인증 실패 (401)
    public UnauthorizedException(String message) {
        super(message);
    }
}
