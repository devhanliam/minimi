package com.minimi.domain.user.request;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Data
@AllArgsConstructor
public class LoginForm {

    @Email
    @NotEmpty(message = "이메일은 필수 입력 사항입니다")
    private String email;

    @NotEmpty(message = "비밀번호는 필수 입력 사항입니다")
    private String password;
}
