package com.minimi.user.security.exception.hanlder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.minimi.domain.user.exception.ErrorCode;
import com.minimi.domain.user.response.ErrorForm;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AuthenticationExceptionHandler implements AuthenticationEntryPoint {
    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        ErrorForm errorForm = ErrorForm.builder()
                .statusCode(HttpStatus.OK.value())
                .message(authException.getMessage())
                .errorCode(ErrorCode.EXPIRED_TOKEN.getCode())
                .build();
        objectMapper.writeValue(response.getWriter(), errorForm);
    }
}
