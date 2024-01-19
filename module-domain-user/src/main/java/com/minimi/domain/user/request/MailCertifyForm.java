package com.minimi.domain.user.request;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@Builder
public class MailCertifyForm {
    @NotNull(message = "인증코드를 입력해주세요")
    @NotEmpty(message = "인증코드를 입력해주세요")
    private String authCode;
    @NotNull(message = "메일인증을 진행해주세요")
    @NotEmpty(message = "메일인증을 진행해주세요")
    private String encAuthCode;
    @NotNull(message = "메일을 입력 후 진행해주세요")
    @NotEmpty(message = "메일을 입력 후 진행해주세요")
    private String email;
}
