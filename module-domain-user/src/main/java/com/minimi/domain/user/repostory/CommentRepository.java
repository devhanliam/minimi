package com.minimi.domain.user.repostory;

import com.minimi.domain.user.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment,Long>, PostRepositoryCustom{
    List<Comment> findByBoardId(Long boardId);
}
