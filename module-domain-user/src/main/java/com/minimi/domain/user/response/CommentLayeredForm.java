package com.minimi.domain.user.response;

import com.minimi.domain.user.entity.Comment;
import com.minimi.domain.user.helper.EntityCovertLayeredFormHelper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentLayeredForm {
    private Long id;
    private String content;
    private String nickName;
    private String email;
    private List<CommentLayeredForm> children = new ArrayList<>();

    public static List<CommentLayeredForm> entityToDto(List<Comment> comments){
        EntityCovertLayeredFormHelper entityCovertLayeredFormHelper = EntityCovertLayeredFormHelper.newInstance(
                comments,
                c -> new CommentLayeredForm(c.getId(),
                        c.getDelFlag() ? null : c.getContent(),
                        c.getDelFlag() ? null : c.getWriter().getNickName(),
                        c.getDelFlag() ? null : c.getWriter().getEmail(),
                        new ArrayList<>()),
                c -> c.getParent(),
                c -> c.getId(),
                d -> d.getChildren()
        );
        return entityCovertLayeredFormHelper.convert();
    }

}
