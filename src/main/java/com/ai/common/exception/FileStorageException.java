package com.ai.common.exception;

public class FileStorageException extends RuntimeException { // 서버 파일 시스템 오류 (500)
    public FileStorageException(String message) {
        super(message);
    }

    public FileStorageException(String message, Throwable cause) {
        super(message, cause);
    }
}
