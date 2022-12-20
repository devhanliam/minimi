package com.minimi.domain.user.service;

import com.minimi.core.FileUtils;
import com.minimi.domain.user.entity.Board;
import com.minimi.domain.user.entity.BoardAttach;
import com.minimi.domain.user.entity.Comment;
import com.minimi.domain.user.entity.User;
import com.minimi.domain.user.exception.ErrorCode;
import com.minimi.domain.user.exception.FileException;
import com.minimi.domain.user.exception.NotFoundBoardException;
import com.minimi.domain.user.exception.NotFoundUserException;
import com.minimi.domain.user.repostory.CommentRepository;
import com.minimi.domain.user.repostory.PostRepository;
import com.minimi.domain.user.repostory.UserRepository;
import com.minimi.domain.user.request.CommentInsertForm;
import com.minimi.domain.user.request.PostCreatForm;
import com.minimi.domain.user.request.PostUpdateForm;
import com.minimi.domain.user.response.CommentLayeredForm;
import com.minimi.domain.user.response.PostInfoForPaging;
import com.minimi.domain.user.response.PostInfoForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {
    private final static String VIEW_COOKIE = "alreadyViewCookie";
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    public List<PostInfoForm> getPostList() {
        List<PostInfoForm> postFormList = postRepository.findPostList(true).stream()
                .map(info -> PostInfoForm.builder()
                        .postId(info.getId())
                        .writer(info.getWriter().getName())
                        .email(info.getWriter().getEmail())
                        .file(info.getAttachList())
                        .title(info.getTitle())
                        .views(info.getViews())
                        .content(info.getContent())
                        .updateTime(info.getUpdateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                        .writeTime(info.getCreateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                        .build())
                .collect(Collectors.toList());
        return postFormList;
    }

    public PostInfoForPaging getPostListForPage(Long cursorId, Pageable pageable) {
        List<PostInfoForm> postInfoForms = PostInfoForm.entitiesToFormForList(
                postRepository.findPostListForPaging(cursorId, pageable));
        Long nowCursorId =
                postInfoForms.size() > 0 ?
                postInfoForms.get(postInfoForms.size() - 1).getPostId() :
                cursorId;
        return PostInfoForPaging.builder()
                .postInfoFormList(postInfoForms)
                .cursorId(nowCursorId)
                .build();
    }

    @Transactional
    public Long createPost(PostCreatForm postCreatForm,MultipartFile file, User user) {
        Board board = Board.builder()
                .title(postCreatForm.getTitle())
                .content(postCreatForm.getContent())
                .openFlag(postCreatForm.isOpenFlag())
                .writer(user)
                .build();
        if (file != null) {
            fileUploadAndSetBoard(file, board);
        }
        return postRepository.save(board).getId();
    }

    private void fileUploadAndSetBoard(MultipartFile file, Board board) {
        try {
            String storeFileName = FileUtils.fileUpload(file);
            String originalFilename = file.getOriginalFilename();
            BoardAttach boardAttach = BoardAttach.builder()
                    .filePath(FileUtils.getFilePreFix() + storeFileName)
                    .fileSize(file.getSize())
                    .originalName(originalFilename)
                    .fileName(storeFileName + FileUtils.getFileExt(originalFilename))
                    .build();
            boardAttach.setBoard(board);
        } catch (IOException e) {
            throw new FileException(ErrorCode.NOT_FOUND_DIR.getMessage());
        }
    }

    public PostInfoForm getPostInfo(Long boardId) {
        List<Comment> commentByPostIdForEntity = commentRepository.findCommentByPostIdForEntity(boardId);
        List<CommentLayeredForm> comments = CommentLayeredForm.entityToDto(commentByPostIdForEntity);
//        List<CommentInfoForm> comments = commentRepository.findCommentByPostId(boardId);
        return postRepository.findPostById(boardId).map(info -> PostInfoForm.builder()
                .postId(info.getId())
                .writer(info.getWriter().getName())
                .email(info.getWriter().getEmail())
                .file(info.getAttachList())
                .title(info.getTitle())
                .openFlag(info.isOpenFlag())
                .views(info.getViews())
                .commentList(comments)
                .content(info.getContent())
                .updateTime(info.getUpdateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .writeTime(info.getCreateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .build()).get();
    }

    @Transactional
    public void updateViews(Long boardId, HttpServletRequest request, HttpServletResponse response) {
        boolean viewFlag = false;
        Cookie[] cookies = request.getCookies();
        if(cookies != null){
            for (Cookie cookie:cookies) {
                if(cookie.getName().equals(VIEW_COOKIE + boardId)){
                    viewFlag = true;
                }
            }
            if(!viewFlag){
                Board board = postRepository.findById(boardId).get();
                board.increaseViews();
                Cookie cookie = getCookie(boardId);
                response.addCookie(cookie);
            }
        }else{
            Board board = postRepository.findById(boardId).get();
            board.increaseViews();
            Cookie cookie = getCookie(boardId);
            response.addCookie(cookie);
        }
    }

    private Cookie getCookie(Long boardId) {
        Cookie cookie = new Cookie(VIEW_COOKIE + boardId, boardId.toString());
        cookie.setMaxAge(7 * 24 * 60 * 60);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        return cookie;
    }

    @Transactional
    public Long createComment(CommentInsertForm commentInsertForm, User user) {

        Board board = postRepository.findById(commentInsertForm.getBoardId())
                .orElseThrow(() -> new NotFoundBoardException());
        Comment comment = Comment.builder()
                .board(board)
                .content(commentInsertForm.getContent())
                .writer(user)
                .build();
        return commentRepository.save(comment).getId();
    }

    @Transactional
    public Long createReComment(CommentInsertForm commentInsertForm, User user) {
        Board board = postRepository.findById(commentInsertForm.getBoardId())
                .orElseThrow(() -> new NotFoundBoardException());
        Comment parent = commentRepository.findById(commentInsertForm.getCommentId()).get();
        Comment comment = Comment.builder()
                .board(board)
                .content(commentInsertForm.getContent())
                .parent(parent)
                .writer(user)
                .build();
        return commentRepository.save(comment).getId();
    }

    @Transactional
    public void deletePost(Long id, User user) {
        Board board = postRepository.findById(id).orElseThrow(() -> new NotFoundBoardException());
        if(user.getId().equals(board.getWriter().getId())){
            postRepository.deleteById(id);
        }else{
            throw new NotFoundUserException();
        }
    }

    @Transactional
    public void updatePost(PostUpdateForm postUpdateForm, MultipartFile file, User user) {
        Board board = postRepository.findById(postUpdateForm.getId()).orElseThrow(() -> new NotFoundBoardException());
        if(user.getId().equals(board.getWriter().getId())){
            board.setUpdateValue(postUpdateForm.getTitle(), postUpdateForm.getContent());
            if(file != null){
                fileUploadAndSetBoard(file,board);
            }
        }else{
            throw new NotFoundUserException();
        }
    }

    public List<PostInfoForm> getMyPostList(User user) {
        List<Board> boardList = postRepository.findPostListByUser(user);
        return PostInfoForm.entitiesToFormForList(boardList);
    }

}
