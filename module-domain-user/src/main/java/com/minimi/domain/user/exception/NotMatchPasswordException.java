package com.minimi.domain.user.exception;

public class NotMatchPasswordException extends RuntimeException{

    public NotMatchPasswordException() {
        super(ErrorCode.NOT_MATCH_PASSWORD.getMessage());
    }

    public NotMatchPasswordException(String message) {
        super(message);
    }
}
