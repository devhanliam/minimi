package com.minimi.domain.user.repostory;

import com.minimi.domain.user.entity.Board;
import com.minimi.domain.user.entity.Comment;
import com.minimi.domain.user.entity.User;
import com.minimi.domain.user.response.CommentInfoForm;

import java.util.List;
import java.util.Optional;

public interface PostRepositoryCustom {
    List<Board> findPostList(boolean openFlag);
    Optional<Board> findPostById(Long postId);
    List<CommentInfoForm> findCommentByPostId(Long postId);
    List<Comment> findCommentByPostIdForEntity(Long postId);
    List<Board> findPostListByUser(User user);
}