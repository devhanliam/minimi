package com.minimi.domain.user.request;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class CommentInsertForm {
    @NotNull(message = "게시글 일련번호가 누락되었습니다")
    Long boardId;
    Long userId;
    @NotEmpty(message = "로그인 후 이용가능합니다")
    String email;
    Long commentId;
    @NotEmpty(message = "내용을 입력해 주세요")
    String content;

}
