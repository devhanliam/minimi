package com.minimi.user.web.controller;

import com.minimi.domain.user.entity.User;
import com.minimi.domain.user.exception.ExpiredTokenException;
import com.minimi.domain.user.exception.NotFoundUserException;
import com.minimi.domain.user.exception.NotMatchPasswordException;
import com.minimi.domain.user.repostory.UserRepository;
import com.minimi.domain.user.request.DuplicationForm;
import com.minimi.domain.user.request.MailCertifyForm;
import com.minimi.domain.user.response.LoginResponse;
import com.minimi.domain.user.response.UserInfoForm;
import com.minimi.domain.user.service.UserService;
import com.minimi.domain.user.request.JoinForm;
import com.minimi.domain.user.request.LoginForm;
import com.minimi.user.jwt.JwtTokenProvider;
import com.minimi.user.mail.MailService;
import com.minimi.user.service.JwtUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.expression.ExpressionException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import static com.minimi.user.jwt.JwtTokenProvider.AUTHORIZATION_HEADER;

@RestController
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserService userService;
    private final UserRepository userRepository;
    private final JwtUserService jwtUserService;
    private final JwtTokenProvider tokenProvider;
    private final MailService mailService;
    private final PasswordEncoder passwordEncoder;
    @PostMapping("/api/v1/login")
    public ResponseEntity login(@Valid @RequestBody LoginForm loginForm, HttpServletResponse response){
        LoginResponse token = jwtUserService.login(loginForm.getEmail(), loginForm.getPassword());
        Cookie cookie = getSecureCookie(token);
        response.addCookie(cookie);
        token.setRefreshToken(null);
        return ResponseEntity.ok(token);
    }

    @PostMapping("/api/v1/logout")
    public ResponseEntity logout(HttpServletRequest request, HttpServletResponse response){
        jwtUserService.logout(request,response);
        return ResponseEntity.ok(null);
    }


    @PostMapping("/api/v1/re-issue")
    public ResponseEntity reIssue(HttpServletRequest request, HttpServletResponse response){
        LoginResponse token = jwtUserService.reissue(request);
        Cookie cookie = getSecureCookie(token);
        response.addCookie(cookie);
        token.setRefreshToken(null);
        return ResponseEntity.ok(token);
    }

    @Transactional(readOnly = true)
    @PostMapping("/api/v1/join/duplication-check")
    public ResponseEntity duplicationCheck(@Valid @RequestBody DuplicationForm duplicationForm) throws MessagingException {
        boolean result = userService.getDuplication(duplicationForm.getEmail());
        if (result) {
            String encryptAuthCode = mailService.sendCertifyMail(MailCertifyForm.builder()
                    .email(duplicationForm.getEmail())
                    .build());
            return ResponseEntity.status(HttpStatus.OK).body(encryptAuthCode);
        }
            return ResponseEntity.status(HttpStatus.OK).body(false);

    }

    @PostMapping("/api/v1/join/auth-code-check")
    public ResponseEntity authCodeCheck(@Valid @RequestBody MailCertifyForm form) {
        boolean result = userService.getDuplication(form.getEmail());
        if (result) {
            if (!passwordEncoder.matches(form.getAuthCode(), form.getEncAuthCode())) {
                throw new NotMatchPasswordException("인증코드가 일치하지 않습니다");
            }
        }

        return ResponseEntity.status(HttpStatus.OK).body(true);
    }

    @PostMapping("/api/v1/join")
    public ResponseEntity join(@Valid @RequestBody JoinForm joinForm) {
        userService.getDuplication(joinForm.getEmail());
        String id = jwtUserService.join(joinForm);
        return ResponseEntity.status(HttpStatus.CREATED).body(id);
    }

    @GetMapping("/api/v1/user/info")
    public ResponseEntity info(HttpServletRequest request) {
        User user = getUserByToken(request);
        UserInfoForm userInfoForm = UserInfoForm.entityToForm(user);
        return ResponseEntity.status(HttpStatus.OK).body(userInfoForm);
    }

    private Cookie getSecureCookie(LoginResponse token) {
        Cookie cookie = new Cookie("refreshToken", token.getRefreshToken());
        cookie.setMaxAge(7 * 24 * 60 * 60);
        cookie.setPath("/");
        cookie.setHttpOnly(true);

        //운영단계에서는 SSL 적용 후 true
        cookie.setSecure(false);
        return cookie;
    }
    private User getUserByToken(HttpServletRequest request) {
        String token = tokenProvider.resolveToken(request).orElseThrow(ExpiredTokenException::new);
        User user =  userRepository.findByEmail(tokenProvider.getUserId(token))
                .orElseThrow(()->new NotFoundUserException());
        return user;
    }
}
