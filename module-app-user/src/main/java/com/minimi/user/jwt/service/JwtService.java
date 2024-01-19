package com.minimi.user.jwt.service;

import com.minimi.user.redis.RedisService;
import com.minimi.user.security.exception.ExpiredTokenException;
import com.minimi.user.service.CustomUserDetailService;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.time.Duration;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtService {
    private String secretKey = "scrkey";
    private final static long ACCESS_VALID_TIME = 30 * 60 * 1000L; // 30m
    private final static long REFRESH_VALID_TIME = 1000L * 60 * 60 * 24;  // 24h
    private final CustomUserDetailService userDetailsService;
    private final RedisService redisService;
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String BEARER_PREFIX = "Bearer ";

    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    public String createToken(String userId, List<String> roles, long validTime) {
        Claims claims = Jwts.claims().setSubject(userId);
        claims.put("roles", roles);
        Date now = new Date();
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + validTime))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public String createAccessToken(String userId, List<String> roles) {
        return this.createToken(userId, roles, ACCESS_VALID_TIME);
    }

    public String createRefreshToken(String userId, List<String> roles) {
        String token = this.createToken(userId, roles, REFRESH_VALID_TIME);
        redisService.setValues(userId, token, Duration.ofMillis(REFRESH_VALID_TIME));
        return token;
    }

//    public Authentication getAuthentication(String token) {
//        UserDetails userDetails = userDetailsService.loadUserByUsername(this.getUserId(token));
//        userDetails.getAuthorities().stream().forEach(a -> log.info(a.getAuthority()));
//        return new JwtAuthenticationToken(userDetails, "", userDetails.getAuthorities());
//    }

    public String getUserId(String token) {
        try {
            String userId = Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
            return userId;
        } catch (ExpiredJwtException ex) {
            ex.printStackTrace();
            throw new ExpiredTokenException();
        }
    }


    public List<SimpleGrantedAuthority> getRoles(String token) {
        try {
            Object roles = Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token)
                    .getBody()
                    .get("roles");
            List<String> list = (List<String>) roles;
            List<SimpleGrantedAuthority> authorities = list.stream()
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());
            return authorities;
        } catch (ExpiredJwtException ex) {
            ex.printStackTrace();
            throw new ExpiredTokenException();
        }
    }

    public Optional<String> resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            return Optional.of(bearerToken.substring(7));
        } else {
            throw new ExpiredTokenException();
        }
    }

    public boolean validateToken(String jwtToken) {
        try {
            if (redisService.getValues(jwtToken) != null) {
                return false;
            }
            Jws<Claims> claims = Jwts
                    .parser().
                    setSigningKey(secretKey)
                    .parseClaimsJws(jwtToken);
            return !claims.getBody().getExpiration().before(new Date());
        } catch (ExpiredJwtException e) {
            log.info("validationToken 에러");
            log.info(jwtToken);
            e.printStackTrace();
            return false;
        }
    }

    public Long getExpiredTime(String token) {
        Date expiration = Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();
        Long expiredTime = expiration.getTime() - new Date().getTime();
        return expiredTime;
    }
}
