package com.minimi.domain.user.request;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Data
public class DuplicationForm {

    @Email
    @NotEmpty(message = "이메일은 필수 입력 사항입니다")
    private String email;
}
