package com.minimi.user.jwt.provider;

import com.minimi.user.jwt.service.JwtService;
import com.minimi.user.jwt.token.JwtAuthenticationToken;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;

@RequiredArgsConstructor
public class JwtAuthenticationProvider implements AuthenticationProvider {
    private final JwtService jwtService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String jwt = (String) authentication.getPrincipal();
        List<SimpleGrantedAuthority> roles = jwtService.getRoles(jwt);
        Authentication authenticated = new JwtAuthenticationToken(jwt, "", roles);
        return authenticated;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return JwtAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
