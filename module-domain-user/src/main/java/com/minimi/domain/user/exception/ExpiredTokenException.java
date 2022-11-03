package com.minimi.domain.user.exception;

public class ExpiredTokenException extends RuntimeException{
    public ExpiredTokenException() {
        super(ErrorCode.EXPIRED_TOKEN.getMessage());
    }

    public ExpiredTokenException(String message) {
        super(message);
    }
}
