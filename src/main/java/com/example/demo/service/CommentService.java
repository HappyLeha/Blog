package com.example.demo.service;

import com.example.demo.dto.CommentDTO;
import com.example.demo.entity.Comment;
import com.example.demo.entity.User;

import java.security.Principal;

public interface CommentService {
    void createComment(CommentDTO commentDTO, User user);
    Comment getComment(int id, int postId);
    void deleteComment(int id, int postId, User user);
}
