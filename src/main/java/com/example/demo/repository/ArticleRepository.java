package com.example.demo.repository;

import com.example.demo.entity.Article;
import com.example.demo.entity.ResetCode;
import com.example.demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ArticleRepository extends JpaRepository<Article,Integer> {
    List<Article> findAllByUser(User user);
}
