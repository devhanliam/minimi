package com.minimi.domain.user.exception;

public class NotFoundUserException extends RuntimeException{

    public NotFoundUserException() {
        super(ErrorCode.NOT_FOUND_USER.getMessage());
    }

    public NotFoundUserException(String message) {
        super(message);
    }
}
