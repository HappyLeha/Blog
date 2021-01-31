package com.example.demo.service;

import com.example.demo.dto.ArticleDTO;
import com.example.demo.entity.Article;
import com.example.demo.entity.User;
import com.example.demo.enumeration.Status;
import com.example.demo.exception.NotEnoughRightException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.ArticleRepository;
import com.example.demo.repository.TagRepository;
import com.example.demo.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.security.Principal;
import java.util.Date;
import java.util.List;

@Service
@Transactional
@Slf4j
public class ArticleServiceImpl implements ArticleService {
    private ArticleRepository articleRepository;
    private TagRepository tagRepository;
    //private UserRepository userRepository;

    @Autowired
    public ArticleServiceImpl(ArticleRepository articleRepository,
                              TagRepository tagRepository) {
        this.articleRepository = articleRepository;
        this.tagRepository = tagRepository;
    }

    @Override
    public void createArticle(Article article) {
        //User user = userRepository.findById(articleDTO.getAuthorId()).get();
        //Article article;

        /*article = new Article(articleDTO.getTitle(), articleDTO.getText(),
                Status.valueOf(articleDTO.getStatus()),user);*/
        articleRepository.save(article);
        log.info("Article "+article+" was created.");
    }

    @Override
    public List<Article> getMyArticles(User user) {
        return articleRepository.findAllByUser(user);
    }

    @Override
    public void updateArticle(int id, ArticleDTO articleDTO,
                              User user) {
        Article article;

        if (!articleRepository.existsById(id)) {
            log.info("Article with id "+id+" doesn't exist.");
            throw new ResourceNotFoundException("This Article doesn't exist");
        }
        article = articleRepository.findById(id).get();
        if (!article.getUser().equals(user)) {
            log.info("User "+user+" isn't author of article "+article);
            throw new NotEnoughRightException("You aren't author of this article.");
        }
        article.setTitle(articleDTO.getTitle());
        article.setText(articleDTO.getText());
        article.setStatus(Status.valueOf(articleDTO.getStatus()));
        article.setUpdatedAt(new Date());
        articleRepository.save(article);
        log.info("Article "+article+" has been updated");
    }

    @Override
    public void deleteArticle(int id, User user) {
        Article article;

        if (!articleRepository.existsById(id)) {
            log.info("Article with id "+id+" doesn't exist.");
            return;
        }
        article = articleRepository.findById(id).get();
        if (!article.getUser().equals(user)) {
            log.info("User "+user+" isn't author of article "+article);
            throw new NotEnoughRightException("You aren't author of this article.");
        }
        articleRepository.delete(article);
        log.info("Article "+article+" has been deleted");
    }

    @Override
    public List<Article> getArticles(String title, User user, Pageable pageable) {
        return articleRepository.findAllByTitleAndUser(title, user, pageable);
    }

    @Override
    public List<Article> getPublicArticles(String title, User user,
                                           Pageable pageable) {
        return articleRepository.findAllByTitleAndUserAndStatus(title, user,
                Status.PUBLIC, pageable);
    }
}
