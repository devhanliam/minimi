package com.minimi.user.security.exception.hanlder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.minimi.domain.user.exception.ErrorCode;
import com.minimi.domain.user.response.ErrorForm;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AccessDeniedHandler implements org.springframework.security.web.access.AccessDeniedHandler {
    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        ErrorForm errorForm = ErrorForm.builder()
                .statusCode(HttpStatus.OK.value())
                .message(accessDeniedException.getMessage())
                .errorCode(ErrorCode.EXPIRED_TOKEN.getCode())
                .build();
        objectMapper.writeValue(response.getWriter(), errorForm);
    }
}
