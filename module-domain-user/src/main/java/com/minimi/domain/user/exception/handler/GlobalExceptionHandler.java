package com.minimi.domain.user.exception.handler;


import com.minimi.domain.user.exception.*;
import com.minimi.domain.user.response.ErrorForm;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<List<ErrorForm>> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        List<ErrorForm> errorForms = getErrorForms(ex.getBindingResult());
        return new ResponseEntity<>(errorForms, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(NotFoundUserException.class)
    protected ResponseEntity<ErrorForm> handleNotFoundUserException(NotFoundUserException ex) {
        ErrorForm errorForm = ErrorForm.builder()
                .statusCode(HttpStatus.OK.value())
                .message(ex.getMessage())
                .errorCode(ErrorCode.NOT_FOUND_USER.getCode())
                .build();
        return new ResponseEntity<>(errorForm, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotMatchPasswordException.class)
    protected ResponseEntity<ErrorForm> handleNotMatchPasswordException(NotMatchPasswordException ex) {
        ErrorForm errorForm = ErrorForm.builder()
                .statusCode(HttpStatus.OK.value())
                .message(ex.getMessage())
                .errorCode(ErrorCode.NOT_MATCH_PASSWORD.getCode())
                .build();
        return new ResponseEntity<>(errorForm, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DuplicatedEmailException.class)
    protected ResponseEntity<ErrorForm> handleDuplicatedEmailException(DuplicatedEmailException ex) {
        ErrorForm errorForm = ErrorForm.builder()
                .statusCode(HttpStatus.OK.value())
                .message(ex.getMessage())
                .errorCode(ErrorCode.DUPLICATED_EMAIL.getCode())
                .build();
        return new ResponseEntity<>(errorForm, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ExpiredTokenException.class)
    protected ResponseEntity<ErrorForm> handleExpiredTokenException(ExpiredTokenException ex) {
        ErrorForm errorForm = ErrorForm.builder()
                .statusCode(HttpStatus.OK.value())
                .message(ex.getMessage())
                .errorCode(ErrorCode.EXPIRED_TOKEN.getCode())
                .build();
        return new ResponseEntity<>(errorForm, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(FileException.class)
    protected ResponseEntity<ErrorForm> handleFileException(FileException ex) {
        ErrorForm errorForm = ErrorForm.builder()
                .statusCode(HttpStatus.OK.value())
                .message(ex.getMessage())
                .errorCode(ErrorCode.NOT_FOUND_DIR.getCode())
                .build();
        return new ResponseEntity<>(errorForm, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @ExceptionHandler(NotFoundBoardException.class)
    protected ResponseEntity<ErrorForm> handleNotFoundBoardException(NotFoundBoardException ex) {
        ErrorForm errorForm = ErrorForm.builder()
                .statusCode(HttpStatus.OK.value())
                .message(ex.getMessage())
                .errorCode(ErrorCode.NOT_FOUND_BOARD.getCode())
                .build();
        return new ResponseEntity<>(errorForm, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    protected List<ErrorForm> getErrorForms(BindingResult bindingResult) {
        List<ErrorForm> errors = new ArrayList<>();
        bindingResult.getAllErrors()
                .forEach(objectError->{
                    errors.add(
                            ErrorForm.builder()
                                    .statusCode(HttpStatus.BAD_REQUEST.value())
                                    .message(objectError.getDefaultMessage())
                                    .build()
                    );
                });
        return errors;
    }
}
