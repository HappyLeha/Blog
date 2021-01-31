package com.example.demo.service;

import com.example.demo.dto.ArticleDTO;
import com.example.demo.dto.TagDTO;
import com.example.demo.dto.TagsCloudDTO;
import com.example.demo.entity.Article;
import com.example.demo.entity.Tag;
import com.example.demo.entity.User;
import com.example.demo.enumeration.Status;
import com.example.demo.exception.NotEnoughRightException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.ArticleRepository;
import com.example.demo.repository.TagRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class ArticleServiceImpl implements ArticleService {

    private ArticleRepository articleRepository;
    private TagRepository tagRepository;

    @Autowired
    public ArticleServiceImpl(ArticleRepository articleRepository,
                              TagRepository tagRepository) {
        this.articleRepository = articleRepository;
        this.tagRepository = tagRepository;
    }

    @Override
    public void createArticle(Article article) {
        articleRepository.save(article);
        log.info("Article " + article + " has been created.");
    }

    @Override
    public List<Article> getMyArticles(User user) {
        return articleRepository.findAllByUser(user);
    }

    @Override
    public void updateArticle(int id, ArticleDTO articleDTO,
                              User user) {
        Article article;
        List<Tag> tags;

        if (!articleRepository.existsById(id)) {
            log.info("Article with id " + id + " doesn't exist.");
            throw new ResourceNotFoundException("This Article doesn't exist");
        }
        article = articleRepository.findById(id).get();
        if (!article.getUser().equals(user)) {
            log.info("User " + user + " isn't author of article " + article);
            throw new NotEnoughRightException("You aren't author of this article.");
        }
        tags = getTagsByNames(articleDTO.getTags());
        article.setTitle(articleDTO.getTitle());
        article.setText(articleDTO.getText());
        article.setStatus(Status.valueOf(articleDTO.getStatus()));
        article.setTags(tags);
        article.setUpdatedAt(new Date());
        articleRepository.save(article);
        deleteUnusedTags();
        log.info("Article " + article + " has been updated");
    }

    @Override
    public void deleteArticle(int id, User user) {
        Article article;

        if (!articleRepository.existsById(id)) {
            log.info("Article with id " + id + " doesn't exist.");
            return;
        }
        article = articleRepository.findById(id).get();
        if (!article.getUser().equals(user)) {
            log.info("User " + user + " isn't author of article " + article);
            throw new NotEnoughRightException("You aren't author of this article.");
        }
        articleRepository.delete(article);
        deleteUnusedTags();
        log.info("Article " + article + " has been deleted");
    }

    @Override
    public List<Article> getArticles(String tags, int skip, int limit,
                                     String title, Integer author, Sort sort,
                                     boolean isUser) {
        List<Article> articles;

        if (isUser) {
            articles = articleRepository.findAllByTitleAndAuthor(title, author, sort);
        }
        else {
            articles = articleRepository.findAllPublicByTitleAndAuthor(title, author, sort);
        }
        if (skip!=0) {
            articles = articles.stream().skip(skip).collect(Collectors.toList());
        }
        if (limit!=0) {
            articles = articles.stream().limit(limit).collect(Collectors.toList());
        }
        if (tags != null) {
            String[] array = tags.split(",");
            List<String> listTags = Arrays.asList(array);
            articles = articles.stream().filter(i->{
                List<String> names = i.getTags().stream().map(Tag::getName).
                        collect(Collectors.toList());
                return names.stream().anyMatch(listTags::contains);
            }).collect(Collectors.toList());
        }
        return articles;
    }

    @Override
    public List<Tag> getTagsByNames(List<TagDTO> tagDTOs) {
        List<Tag> tags = new ArrayList<>();
        Set<Tag> tagSet;

        tagDTOs.forEach(i->{
            if (tagRepository.existsByName(i.getName())) {
                tags.add(tagRepository.findByName(i.getName()));
            }
            else {
                tagRepository.save(new Tag(i.getName()));
                tags.add(tagRepository.findByName(i.getName()));
            }
        });
        tagSet = new HashSet<>(tags);
        return new ArrayList<>(tagSet);
    }

    @Override
    public void deleteUnusedTags() {
        List<Tag> tags = tagRepository.findAll();

        tags.forEach(i->{
            if (i.getArticles() == null || i.getArticles().size() == 0) {
                tagRepository.delete(i);
            }
        });
    }

    @Override
    public List<TagsCloudDTO> getTagsCloud() {
        List<Tag> tags = tagRepository.findAll();
        List<TagsCloudDTO> tagsCloud = new ArrayList<>();

        tags.forEach(i->{
            tagsCloud.add(new TagsCloudDTO(i.getName(),i.getArticles().size()));
        });
        return tagsCloud;
    }
}
