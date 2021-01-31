package com.example.demo.controller;

import com.example.demo.dto.ArticleDTO;
import com.example.demo.dto.CommentDTO;
import com.example.demo.entity.Article;
import com.example.demo.entity.Comment;
import com.example.demo.entity.User;
import com.example.demo.enumeration.Status;
import com.example.demo.service.ArticleService;
import com.example.demo.service.CommentService;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;

@RestController
@RequestMapping("articles/{postId}/comments")
public class CommentController {
    private CommentService commentService;
    private UserService userService;

    @Autowired
    public CommentController(CommentService commentService,
                             UserService userService) {
        this.commentService = commentService;
        this.userService = userService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createComment(@RequestBody @Valid CommentDTO commentDTO,
                              @PathVariable("postId") int postId, Principal principal) {
        User user = userService.getProfile(principal);

        commentDTO.setPostId(postId);
        commentService.createComment(commentDTO, user);
    }

    @GetMapping("{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public CommentDTO getComment(@PathVariable("postId") int postId,
                              @PathVariable("id") int id) {
        Comment comment = commentService.getComment(id, postId);
        return new CommentDTO(comment.getId(), comment.getMessage(),
                comment.getCreatedAt(), comment.getArticle().getId(),
                comment.getUser().getId());
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteComment(@PathVariable("postId") int postId,
                                 @PathVariable("id") int id,
                                    Principal principal) {
        User user = userService.getProfile(principal);

        commentService.deleteComment(id, postId, user);
    }
}
