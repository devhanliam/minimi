package com.minimi.user.security.exception;

import com.minimi.domain.user.exception.ErrorCode;
import org.springframework.security.core.AuthenticationException;

public class ExpiredTokenException extends AuthenticationException {
    public ExpiredTokenException() {
        super(ErrorCode.EXPIRED_TOKEN.getMessage());
    }

    public ExpiredTokenException(String message) {
        super(message);
    }
}
