package com.minimi.domain.user.exception;

public class NotFoundBoardException extends RuntimeException{

    public NotFoundBoardException() {
        super(ErrorCode.NOT_FOUND_BOARD.getMessage());
    }

    public NotFoundBoardException(String message) {
        super(message);
    }
}
