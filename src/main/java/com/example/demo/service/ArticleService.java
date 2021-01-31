package com.example.demo.service;

import com.example.demo.dto.ArticleDTO;
import com.example.demo.dto.TagDTO;
import com.example.demo.dto.TagsCloudDTO;
import com.example.demo.entity.Article;
import com.example.demo.entity.Tag;
import com.example.demo.entity.User;
import org.springframework.data.domain.Sort;
import java.util.List;

public interface ArticleService {

    void createArticle(Article article);

    List<Article> getMyArticles(User user);

    void updateArticle(int id, ArticleDTO articleDTO, User user);

    void deleteArticle(int id, User user);

    List<Tag> getTagsByNames(List<TagDTO> tags);

    List<Article> getArticles(String tags, int skip, int limit,
                              String title, Integer author, Sort sort,
                              boolean isUser);

    void deleteUnusedTags();

    List<TagsCloudDTO> getTagsCloud();
}
