package com.minimi.domain.user.exception;

public class DuplicatedEmailException extends RuntimeException{

    public DuplicatedEmailException() {
        super(ErrorCode.DUPLICATED_EMAIL.getMessage());
    }

    public DuplicatedEmailException(String message) {
        super(message);
    }
}
