package com.minimi.domain.user.response;

import com.minimi.domain.user.entity.BoardAttach;
import com.minimi.domain.user.entity.Comment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class PostInfoForm {
    private String title;
    private String content;
    private String writer;
    private String email;
    private String writeTime;
    private String updateTime;
    private boolean openFlag;
    private Long postId;
    private Long views;
    private List<BoardAttach> file;
    private List<CommentInfoForm> commentList;
}
