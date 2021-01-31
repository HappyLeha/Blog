package com.example.demo.repository;

import com.example.demo.entity.Article;
import com.example.demo.entity.ResetCode;
import com.example.demo.entity.User;
import com.example.demo.enumeration.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ArticleRepository extends JpaRepository<Article,Integer> {
    List<Article> findAllByUser(User user);
    List<Article> findAllByTitleAndUser(String title, User user, Pageable pageable);
    List<Article> findAllByTitleAndUserAndStatus(String title, User user,
                                                 Status status, Pageable pageable);
}
