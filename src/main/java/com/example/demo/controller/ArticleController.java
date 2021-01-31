package com.example.demo.controller;

import com.example.demo.dto.ArticleDTO;
import com.example.demo.dto.UserDTO;
import com.example.demo.entity.Article;
import com.example.demo.entity.User;
import com.example.demo.enumeration.Status;
import com.example.demo.service.ArticleService;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

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
    @PreAuthorize("isAuthenticated()")
    @ResponseStatus(HttpStatus.CREATED)
    public void createArticle(@RequestBody @Valid ArticleDTO articleDTO,
                              Principal principal) {
        User user = userService.getProfile(principal);

        articleService.createArticle(new Article(articleDTO.getTitle(),
                articleDTO.getText(), Status.valueOf(articleDTO.getStatus()),
                user));
    }

    @GetMapping("/my")
    @ResponseStatus(HttpStatus.OK)
    public List<ArticleDTO> getMyArticles(Principal principal) {
        User user = userService.getProfile(principal);
        List<ArticleDTO> articleDTOs = new ArrayList<>();

        List<Article> articles = articleService.getMyArticles(user);
        articles.forEach(i->articleDTOs.add(new ArticleDTO(i.getId(),i.getTitle(),
                i.getText(),i.getStatus().name(),i.getUser().getId(),i.getCreatedAt(),
                i.getUpdatedAt())));
        return articleDTOs;
    }

    @PutMapping("/articles/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void updateArticle(@PathVariable("id") int id,
                              @RequestBody @Valid ArticleDTO articleDTO,
                              Principal principal) {
        User user = userService.getProfile(principal);

        articleService.updateArticle(id, articleDTO, user);
    }

    @DeleteMapping("/articles/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteArticle(@PathVariable("id") int id,
                              Principal principal) {
        User user = userService.getProfile(principal);

        articleService.deleteArticle(id, user);
    }

    @GetMapping("/articles")
    @ResponseStatus(HttpStatus.OK)
    public List<ArticleDTO> getArticles(Principal principal, @RequestParam int skip,
                                        @RequestParam int limit,
                                        @RequestParam String q,
                                        @RequestParam String author,
                                        @RequestParam String sort,
                                        @RequestParam String order) {
        return null;

    }
}
