package com.example.demo.service;

import com.example.demo.dto.CommentDTO;
import com.example.demo.entity.Article;
import com.example.demo.entity.Comment;
import com.example.demo.entity.User;
import com.example.demo.exception.NotEnoughRightException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.ArticleRepository;
import com.example.demo.repository.CommentRepository;
import com.example.demo.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.security.Principal;

@Service
@Transactional
@Slf4j
public class CommentServiceImpl implements CommentService {
    private CommentRepository commentRepository;
    private ArticleRepository articleRepository;
    private UserRepository userRepository;

    public CommentServiceImpl(CommentRepository commentRepository,
                              ArticleRepository articleRepository,
                              UserRepository userRepository) {
        this.commentRepository = commentRepository;
        this.articleRepository = articleRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void createComment(CommentDTO commentDTO, User user) {
        Article article;
        Comment comment;

        if (!articleRepository.existsById(commentDTO.getPostId())) {
           log.info("Article with id "+commentDTO.getPostId()+" doesn't exist.");
           throw new ResourceNotFoundException("This Article doesn't exist");
        }
        article = articleRepository.findById(commentDTO.getPostId()).get();
        comment = new Comment(commentDTO.getMessage(), article, user);
        commentRepository.save(comment);
        log.info("Comment "+comment+" was created.");
    }

    @Override
    public Comment getComment(int id, int postId) {
        if (!articleRepository.existsById(postId)) {
            log.info("Article with id "+postId+" doesn't exist.");
            throw new ResourceNotFoundException("This Article doesn't exist");
        }
        if (!commentRepository.existsById(id)) {
            log.info("Comment with id "+postId+" doesn't exist.");
            throw new ResourceNotFoundException("This Comment doesn't exist");
        }
        return commentRepository.findById(id).get();
    }

    @Override
    public void deleteComment(int id, int postId, User user) {
        Comment comment;

        if (!articleRepository.existsById(postId)) {
            log.info("Article with id "+postId+" doesn't exist.");
            throw new ResourceNotFoundException("This Article doesn't exist");
        }
        if (!commentRepository.existsById(id)) {
            return;
        }
        comment = commentRepository.findById(id).get();
        if (!comment.getUser().equals(user)&&!comment.getArticle().getUser().
                equals(user)) {
            log.info("User "+user+" can't delete "+comment);
            throw new NotEnoughRightException("You can't delete this article.");
        }
        commentRepository.delete(comment);
    }
}
