package com.minimi.domain.user.request;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
@AllArgsConstructor
public class PostCreatForm {

    @NotEmpty(message = "제목은 필수입력 항목 입니다")
    private String title;

    @NotEmpty(message = "내용은 필수입력 항목 입니다")
    private String content;
    private boolean openFlag;

}
