package com.minimi.user.jwt.filter;

import com.minimi.user.jwt.service.JwtService;
import com.minimi.user.jwt.token.JwtAuthenticationToken;
import com.minimi.user.security.exception.ExpiredTokenException;
import com.minimi.user.security.exception.hanlder.AuthenticationExceptionHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private AuthenticationEntryPoint authenticationEntryPoint = new AuthenticationExceptionHandler();
    private final AuthenticationManager authenticationManager;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            Optional<String> token = jwtService.resolveToken((HttpServletRequest) request);
            if (token.isPresent() && jwtService.validateToken(token.get())) {
                JwtAuthenticationToken jwtAuthenticationToken = new JwtAuthenticationToken(token.get(), "");
//                Authentication authentication = jwtService.getAuthentication(token.get());
                Authentication authentication = authenticationManager.authenticate(jwtAuthenticationToken);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
            filterChain.doFilter(request, response);
        } catch (ExpiredTokenException ex) {
            authenticationEntryPoint.commence(request, response, ex);
        }
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String[] excludePath = {"/api/v1/login", "/api/v1/logout", "/api/v1/re-issue", "/api/v1/join/", "/api/v1/post/", "/api/v1/post/image/"};
        String path = request.getRequestURI();
        return Arrays.stream(excludePath).anyMatch(path::startsWith);
    }
}
