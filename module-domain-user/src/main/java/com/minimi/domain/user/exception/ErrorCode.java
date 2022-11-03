package com.minimi.domain.user.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorCode {

    NOT_FOUND_USER(100, "NOT_FOUND_USER", "가입된 회원이 아닙니다"),
    NOT_MATCH_PASSWORD(110, "NOT_MATCH_PASSWORD", "패스워드가 일치하지 않습니다"),
    DUPLICATED_EMAIL(120, "DUPLICATED_EMAIL", "중복된 이메일입니다"),
    EXPIRED_TOKEN(130,"EXPIRED_TOKEN","만료된 토큰입니다"),
    NOT_FOUND_DIR(140,"NOT_FOUND_DIR","디렉토리를 찾을 수 없습니다"),
    NOT_FOUND_FILE(150, "NOT_FOUND_FILE", "파일을 찾을 수 없습니다"),
    NOT_FOUND_BOARD(160,"NOT_FOUND_BOARD","게시물을 찾을 수 없습니다")
    ;


    private int id;
    private String code;
    private String message;



}
