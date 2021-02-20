package com.example.demo.controller;

import com.example.demo.dto.ArticleDTO;
import com.example.demo.dto.TagDTO;
import com.example.demo.dto.TagsCloudDTO;
import com.example.demo.entity.Article;
import com.example.demo.entity.Tag;
import com.example.demo.entity.User;
import com.example.demo.enumeration.Status;
import com.example.demo.service.ArticleService;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@RestController
@Validated
public class ArticleController {
    private ArticleService articleService;
    private UserService userService;

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
        List<Tag> tags = new ArrayList<>();

        if (articleDTO.getTags() != null) {
            tags = articleService.getTagsByNames(articleDTO.getTags());
        }
        articleService.createArticle(new Article(articleDTO.getTitle(),
                articleDTO.getText(), Status.valueOf(articleDTO.getStatus()),
                user,tags));
    }

    @GetMapping("/my")
    @ResponseStatus(HttpStatus.OK)
    public List<ArticleDTO> getMyArticles(Principal principal) {
        User user = userService.getProfile(principal);
        List<ArticleDTO> articleDTOs = new ArrayList<>();

        List<Article> articles = articleService.getMyArticles(user);
        articles.forEach(article -> {
            List<TagDTO> tags = new ArrayList<>();

            article.getTags().forEach(tag -> tags.add(new TagDTO(tag.getId(),
                                                                 tag.getName())));
            articleDTOs.add(new ArticleDTO(article.getId(), article.getTitle(),
                                           article.getText(), article.getStatus().name(),
                                           article.getUser().getId(), article.getCreatedAt(),
                                           article.getUpdatedAt(), tags));
        });
        return articleDTOs;
    }

    @GetMapping("tags-cloud")
    @ResponseStatus(HttpStatus.OK)
    public List<TagsCloudDTO> getTagsCloud() {
        return articleService.getTagsCloud();
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
    public List<ArticleDTO> getArticles(Principal principal,
                                        @RequestParam(required = false) String tags,
                                        @RequestParam(required = false,
                                                      defaultValue = "0") @Min(0) int skip,
                                        @RequestParam(required = false,
                                                      defaultValue = "0") @Min(0) int limit,
                                        @RequestParam(required = false) String q,
                                        @RequestParam(required = false) int author,
                                        @RequestParam(required = false,
                                                      defaultValue = "id")
                                            @Pattern(regexp = "^id$|^title$|^text$|^status$")
                                                     String sort,
                                        @RequestParam(required = false,
                                                      defaultValue = "ASC")
                                            @Pattern(regexp = "^ASC$|^DESC$")
                                                     String order) {
        Sort.Direction sortDirection;
        Sort sortObject;
        List<Article> articles;
        List<ArticleDTO> articleDTOs = new ArrayList<>();

        if (order.equals("ASC")) {
            sortDirection = Sort.Direction.ASC;
        }
        else {
            sortDirection = Sort.Direction.DESC;
        }
        sortObject = Sort.by(sortDirection, sort);
        if (principal == null) {
            articles = articleService.getArticles(tags,skip, limit, q, author,
                    sortObject, false);
        }
        else {
            articles = articleService.getArticles(tags,skip, limit, q, author,
                    sortObject, true);
        }
        articles.forEach(article -> {
            List<TagDTO> tagDTOs = new ArrayList<>();

            article.getTags().forEach(tag -> tagDTOs.add(new TagDTO(tag.getId(),
                                                                    tag.getName())));
            articleDTOs.add(new ArticleDTO(article.getId(), article.getTitle(),
                                           article.getText(),
                                           article.getStatus().name(),
                                           article.getUser().getId(),
                                           article.getCreatedAt(),
                                           article.getUpdatedAt(), tagDTOs));
        });
        return articleDTOs;
    }
}
