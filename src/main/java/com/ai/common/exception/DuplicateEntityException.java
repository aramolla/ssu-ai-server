package com.ai.common.exception;

public class DuplicateEntityException extends RuntimeException { //  중복된 리소스 생성 시도 (409)
    public DuplicateEntityException(String message) {
        super(message);
    }
}