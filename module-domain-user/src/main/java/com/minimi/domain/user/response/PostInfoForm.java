package com.minimi.domain.user.response;

import com.minimi.domain.user.entity.Board;
import com.minimi.domain.user.entity.BoardAttach;
import com.minimi.core.helper.EntityCovertFormHelper;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.format.DateTimeFormatter;
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
    private String open;
    private List<BoardAttach> file;
    private List<CommentLayeredForm> commentList;

    public static List<PostInfoForm> entitiesToFormForList(List<Board> entities) {
        EntityCovertFormHelper entityCovertFormHelper = EntityCovertFormHelper.newInstanceForList(entities,
                e -> PostInfoForm.builder()
                        .title(e.getTitle())
                        .postId(e.getId())
                        .writer(e.getWriter().getNickName())
                        .views(e.getViews())
                        .content(e.getContent())
                        .file(e.getAttachList())
                        .writeTime(e.getCreateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                        .open(e.isOpenFlag() ? "공개" : "비공개")
                        .build()
        );
        return entityCovertFormHelper.convertList();
    }
}
