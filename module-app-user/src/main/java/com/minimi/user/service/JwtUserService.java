package com.minimi.user.service;

import com.minimi.domain.user.entity.Role;
import com.minimi.domain.user.entity.User;
import com.minimi.domain.user.exception.ExpiredTokenException;
import com.minimi.domain.user.exception.NotFoundUserException;
import com.minimi.domain.user.exception.NotMatchPasswordException;
import com.minimi.domain.user.repostory.UserRepository;
import com.minimi.domain.user.request.JoinForm;
import com.minimi.domain.user.response.LoginResponse;
import com.minimi.user.jwt.JwtTokenProvider;
import com.minimi.user.redis.RedisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.Duration;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class JwtUserService {
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final RedisService redisService;

    public LoginResponse login(String email, String password){
        User user  = userRepository.findByEmail(email).orElseThrow(()->new NotFoundUserException());
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new NotMatchPasswordException();
        }
        String accessToken = jwtTokenProvider.createAccessToken(user.getEmail(), user.getRoles());
        String refreshToken = jwtTokenProvider.createRefreshToken(user.getEmail(), user.getRoles());
        return LoginResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .email(user.getEmail())
                .name(user.getName())
                .nickName(user.getNickName())
                .build();
    }

    public LoginResponse reissue(HttpServletRequest request){
        if(request.getCookies() == null){
            log.info("Token reissue null check");
            throw new ExpiredTokenException();
        }

        log.info("Token reissue resolve start");
        Optional<String> resolveToken = jwtTokenProvider.resolveToken(request);
        Cookie cookie = Arrays.stream(request.getCookies())
                .filter(c -> c.getName().equals("refreshToken"))
                .findFirst()
                .orElseThrow(()->new ExpiredTokenException());
        log.info("Token reissue valid start");
        if (jwtTokenProvider.validateToken(cookie.getValue())) {
            if (resolveToken.isPresent()) {
                String accessToken = resolveToken.get();
                Authentication authentication = jwtTokenProvider.getAuthentication(accessToken);
                String refreshToken = redisService.getValues(authentication.getName());
                if (refreshToken.equals(cookie.getValue())) {
                    User user = userRepository.findByEmail(authentication.getName()).orElseThrow(()-> new NotFoundUserException());
                    String newAccessToken = jwtTokenProvider.createAccessToken(user.getEmail(), user.getRoles());
                    String newRefreshToken = jwtTokenProvider.createRefreshToken(user.getEmail(), user.getRoles());
                    redisService.setValues(authentication.getName(),newRefreshToken);
                    return LoginResponse.builder()
                            .accessToken(newAccessToken)
                            .refreshToken(newRefreshToken)
                            .build();
                }else {
//                    redisService.deleteValues(authentication.getName());
                    throw new NotFoundUserException();
                }
            }
        }
        return null;
    }

    @Transactional
    public String join(JoinForm joinForm) {
        User user = User.builder()
                .email(joinForm.getEmail())
                .mobile(joinForm.getMobile())
                .name(joinForm.getName())
                .nickName(joinForm.getNickName())
                .password(passwordEncoder.encode(joinForm.getPassword()))
                .roles(Collections.singletonList(Role.USER.getCode()))
                .build();
        return userRepository.save(user).getEmail();
    }

    public void logout(HttpServletRequest request, HttpServletResponse response) {
        Optional<String> resolveToken = jwtTokenProvider.resolveToken(request);
        if (resolveToken.isPresent()) {
            String accessToken = resolveToken.get();
            Authentication authentication = jwtTokenProvider.getAuthentication(accessToken);
            if(redisService.getValues(authentication.getName()) != null){
                redisService.deleteValues(authentication.getName());
                redisService.setValues(accessToken,"blackList", Duration.ofMillis(jwtTokenProvider.getExpiredTime(accessToken)));
                SecurityContextHolder.clearContext();
            }
        }
    }
}
