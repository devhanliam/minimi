package com.minimi.domain.user.request;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Data
public class JoinForm {

    @NotEmpty(message = "이름은 필수 입력 사항입니다")
    private String name;

    @Email
    @NotEmpty(message = "이메일은 필수 입력 사항입니다")
    private String email;
    @NotEmpty(message = "전화번호는 필수 입력 사항입니다")
    private String mobile;
    @NotEmpty(message = "비밀번호는 필수 입력 사항입니다")
    private String password;

    @NotEmpty(message = "별칭은 필수 입력 사항입니다")
    private String nickName;

}
