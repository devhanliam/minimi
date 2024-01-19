package com.minimi.domain.user.request;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
public class PostUpdateForm {
    @NotNull(message = "게시글 정보를 가져올 수 없습니다.")
    private Long id;
    @NotNull(message = "내용을 입력해주세요.")
    @NotEmpty(message = "내용을 입력해주세요.")
    private String content;
    @NotNull(message = "제목을 입력해주세요.")
    @NotEmpty(message = "제목을 입력해주세요.")
    private String title;
    private boolean openFlag;
}
