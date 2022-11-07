package com.minimi.user.web.controller;

import com.minimi.core.FileUtils;
import com.minimi.domain.user.entity.Board;
import com.minimi.domain.user.entity.User;
import com.minimi.domain.user.exception.ExpiredTokenException;
import com.minimi.domain.user.exception.NotFoundBoardException;
import com.minimi.domain.user.exception.NotFoundUserException;
import com.minimi.domain.user.repostory.PostRepository;
import com.minimi.domain.user.repostory.UserRepository;
import com.minimi.domain.user.request.CommentInsertForm;
import com.minimi.domain.user.request.PostCreatForm;
import com.minimi.domain.user.request.PostUpdateForm;
import com.minimi.domain.user.response.PostInfoForm;
import com.minimi.domain.user.service.PostService;
import com.minimi.user.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.activation.MimetypesFileTypeMap;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class PostController {
    private final PostService postService;
    private final JwtTokenProvider tokenProvider;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    @GetMapping("/api/v1/post/list")
    public ResponseEntity getPostList(){
        List<PostInfoForm> postList = postService.getPostList();
        return ResponseEntity.status(HttpStatus.OK).body(postList);
    }

    @GetMapping("/api/v1/post/info/{boardId}")
    public ResponseEntity getPostInfo(@PathVariable(name = "boardId")Long boardId, HttpServletRequest request, HttpServletResponse response) {
        PostInfoForm postInfo = postService.getPostInfo(boardId);
        postService.updateViews(boardId, request, response);
        return ResponseEntity.status(HttpStatus.OK).body(postInfo);
    }

    @PostMapping("/api/v1/user/post/create")
    public ResponseEntity post(@RequestPart(required = false,value = "file") MultipartFile file,
                               @Valid @RequestPart(value = "data") PostCreatForm postCreatForm,
                               HttpServletRequest request){
        User user = getUserByToken(request);
        Long boardId = postService.createPost(postCreatForm,file,user);
        return ResponseEntity.status(HttpStatus.CREATED).body(boardId);
    }

    @PostMapping("/api/v1/user/post/update")
    public ResponseEntity postUpdate(@RequestPart(required = false, value = "file") MultipartFile file,
                                     @Valid @RequestPart(value = "data") PostUpdateForm postUpdateForm,
                                     HttpServletRequest request) {

        User user = getUserByToken(request);
        postService.updatePost(postUpdateForm, file, user);
        return null;
    }
    @PostMapping("/api/v1/user/post/delete/{id}")
    public ResponseEntity postDelete(@PathVariable(name = "id") Long id, HttpServletRequest request) {
        User user = getUserByToken(request);
        postService.deletePost(id, user);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @PostMapping("/api/v1/user/post/comment/create")
    public ResponseEntity comment(@Valid @RequestBody CommentInsertForm commentInsertForm,HttpServletRequest request){
        User user = getUserByToken(request);
        Long commentId = postService.createComment(commentInsertForm,user);
        return ResponseEntity.status(HttpStatus.CREATED).body(commentId);
    }

    @PostMapping("/api/v1/user/post/re-comment/create")
    public ResponseEntity reComment(@Valid @RequestBody CommentInsertForm commentInsertForm,HttpServletRequest request) {
        User user = getUserByToken(request);
        Long commentId = postService.createReComment(commentInsertForm,user);
        return ResponseEntity.status(HttpStatus.CREATED).body(commentId);
    }

    @GetMapping(value = "/api/v1/post/image/{fileName}")
    public ResponseEntity<Resource> getImageResource(@PathVariable(name = "fileName") String fileName, HttpServletResponse response) throws IOException {
        String fileNameWithoutExt = FileUtils.removeFileExt(fileName);
        String filePath = FileUtils.getFilePreFix() + fileNameWithoutExt + File.separator + fileName;
        File file = new File(filePath) ;
        FileSystemResource fileSystemResource = new FileSystemResource(file.toPath());
        MimetypesFileTypeMap mimeType = new MimetypesFileTypeMap(fileSystemResource.getPath());
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.parseMediaType(mimeType.getContentType(file)))
                .body(fileSystemResource);
    }

    private User getUserByToken(HttpServletRequest request) {
        String token = tokenProvider.resolveToken(request).orElseThrow(ExpiredTokenException::new);
        User user =  userRepository.findByEmail(tokenProvider.getUserId(token))
                .orElseThrow(()->new NotFoundUserException());
        return user;
    }

}
