package com.example.demo.service;

import com.example.demo.dto.CommentDTO;
import com.example.demo.entity.Comment;
import com.example.demo.entity.User;
import org.springframework.data.domain.Sort;
import java.util.List;

public interface CommentService {
    void createComment(CommentDTO commentDTO, User user);
    Comment getComment(int id, int postId);
    void deleteComment(int id, int postId, User user);
    List<Comment> getComments(int skip, int limit, String title, Integer author,
                              Sort sort);
}
