package com.example.demo.controller;

import com.example.demo.dto.CommentDTO;
import com.example.demo.entity.Comment;
import com.example.demo.entity.User;
import com.example.demo.service.CommentService;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

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
                              @PathVariable("postId") int postId,
                              Principal principal) {
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

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<CommentDTO> getArticles(@RequestParam(required = false,
                                                      defaultValue = "0") @Min(0) int skip,
                                        @RequestParam(required = false,
                                                      defaultValue = "0") @Min(0) int limit,
                                        @RequestParam(required = false) String q,
                                        @RequestParam(required = false) Integer author,
                                        @RequestParam(required = false,
                                                      defaultValue = "id")
                                                      @Pattern(regexp = "^id$|^title$|^text$|^status$")
                                                      String sort,
                                        @RequestParam(required = false,
                                                      defaultValue = "ASC")
                                                      @Pattern(regexp = "^ASC$|^DESC$")
                                                      String order) {
        Sort.Direction sortDirection;
        Sort sortObject;
        List<Comment> comments;
        List<CommentDTO> commentDTOs = new ArrayList<>();

        if (order.equals("ASC")) {
            sortDirection = Sort.Direction.ASC;
        }
        else {
            sortDirection = Sort.Direction.DESC;
        }
        sortObject = Sort.by(sortDirection, sort);
        comments = commentService.getComments(skip, limit, q, author, sortObject);

        comments.forEach(i->{
            commentDTOs.add(new CommentDTO(i.getId(), i.getMessage(),
                            i.getCreatedAt(), i.getArticle().getId(),
                            i.getUser().getId()));
        });
        return commentDTOs;
    }
}
