package com.example.demo.service;

import com.example.demo.dto.ArticleDTO;
import com.example.demo.entity.Article;
import com.example.demo.entity.User;
import org.springframework.data.domain.Pageable;

import java.security.Principal;
import java.util.List;

public interface ArticleService {
    void createArticle(Article article);
    List<Article> getMyArticles(User user);
    void updateArticle(int id, ArticleDTO articleDTO, User user);
    void deleteArticle(int id, User user);
    List<Article> getArticles(String title, User user, Pageable pageable);
    List<Article> getPublicArticles(String title, User user, Pageable pageable);
}
