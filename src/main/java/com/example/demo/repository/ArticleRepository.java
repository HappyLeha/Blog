package com.example.demo.repository;

import com.example.demo.entity.Article;
import com.example.demo.entity.ResetCode;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleRepository extends JpaRepository<Article,Integer> {
}
