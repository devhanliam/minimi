package com.minimi.domain.user.repostory.impl;

import com.minimi.domain.user.entity.*;
import com.minimi.domain.user.repostory.PostRepositoryCustom;
import com.minimi.domain.user.response.CommentInfoForm;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static com.minimi.domain.user.entity.QBoard.board;
import static com.minimi.domain.user.entity.QComment.*;
import static com.minimi.domain.user.entity.QUser.*;

@RequiredArgsConstructor
public class PostRepositoryCustomImpl implements PostRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public List<Board> findPostList(boolean openFlag) {
        return queryFactory.selectFrom(board)
                .where(board.openFlag.eq(openFlag))
                .innerJoin(board.writer, user)
                .fetchJoin()
                .leftJoin(board.attachList, QBoardAttach.boardAttach)
                .fetchJoin()
                .orderBy(board.id.desc())
                .fetch();
    }

    @Override
    public Optional<Board> findPostById(Long postId) {
        return Optional.ofNullable(queryFactory.selectFrom(board)
                .where(board.id.eq(postId))
                .innerJoin(board.writer, user)
                .fetchJoin()
                .leftJoin(board.attachList, QBoardAttach.boardAttach)
                .fetchJoin()
//                .leftJoin(QBoard.board.commentList, QComment.comment)
//                .fetchJoin()
                .fetchOne());
    }

    @Override
    public List<CommentInfoForm> findCommentByPostId(Long postId) {
        return queryFactory.
                select(
                        Projections.fields(CommentInfoForm.class
                                , comment.id,comment.content,user.nickName)
                        )
                .from(comment)
                .where(comment.board.id.eq(postId))
                .innerJoin(comment.writer, user)
                .fetch();
    }

    @Override
    public List<Comment> findCommentByPostIdForEntity(Long postId) {
        return queryFactory
                .select(comment)
                .from(comment)
                .leftJoin(comment.parent)
                .fetchJoin()
                .innerJoin(comment.writer,user)
                .fetchJoin()
                .where(comment.board.id.eq(postId))
                .orderBy(
                        comment.parent.id.asc(),
                        comment.createTime.asc()
                )
                .fetch();
    }

    @Override
    public List<Board> findPostListByUser(User user) {
        return queryFactory
                .selectFrom(board)
                .where(board.writer.eq(user))
                .orderBy(
                        board.id.desc()
                )
                .fetch();

    }

    @Override
    public List<Board> findPostListForPaging(Long cursorId, Pageable pageable) {
        return queryFactory.selectFrom(board)
                .where(
                        board.openFlag.eq(true),
                        ltCursorId(cursorId)

                )
                .innerJoin(board.writer, user)
                .fetchJoin()
                .leftJoin(board.attachList, QBoardAttach.boardAttach)
                .fetchJoin()
                .limit(pageable.getPageSize())
                .orderBy(board.id.desc())
                .fetch();
    }

    private BooleanExpression ltCursorId(Long cursorId) {
        return cursorId == null ? null : board.id.lt(cursorId);
    }

}
