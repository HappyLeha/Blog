package com.example.demo.controller;

import com.example.demo.dto.ArticleDTO;
import com.example.demo.dto.UserDTO;
import com.example.demo.entity.User;
import com.example.demo.service.ArticleService;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class ArticleController {
    ArticleService articleService;
    UserService userService;

    @Autowired
    public ArticleController(ArticleService articleService,
                             UserService userService) {
        this.articleService = articleService;
        this.userService = userService;
    }

    @PostMapping("/articles")
    @ResponseStatus(HttpStatus.CREATED)
    public void createArticle(@RequestBody @Valid ArticleDTO articleDTO) {

    }
}
