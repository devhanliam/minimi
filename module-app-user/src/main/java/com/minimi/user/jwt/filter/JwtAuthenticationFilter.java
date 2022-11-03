package com.minimi.user.jwt.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.minimi.domain.user.exception.ErrorCode;
import com.minimi.domain.user.exception.ExpiredTokenException;
import com.minimi.domain.user.response.ErrorForm;
import com.minimi.user.jwt.JwtTokenProvider;
import com.minimi.user.redis.RedisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends GenericFilterBean {
    private final JwtTokenProvider jwtTokenProvider;
    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        try {
            Optional<String> token = jwtTokenProvider.resolveToken((HttpServletRequest) request);
            if (token.isPresent() && jwtTokenProvider.validateToken(token.get())) {
                Authentication authentication = jwtTokenProvider.getAuthentication(token.get());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
            chain.doFilter(request, response);
        } catch (ExpiredTokenException ex) {
            HttpServletResponse httpServletResponse = (HttpServletResponse) response;
            httpServletResponse.setStatus(HttpStatus.BAD_REQUEST.value());
            httpServletResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
            httpServletResponse.setCharacterEncoding("UTF-8");
            ErrorForm errorForm = ErrorForm.builder()
                    .statusCode(HttpStatus.OK.value())
                    .message(ex.getMessage())
                    .errorCode(ErrorCode.EXPIRED_TOKEN.getCode())
                    .build();
            objectMapper.writeValue(httpServletResponse.getWriter(),errorForm);
        }
    }
}
