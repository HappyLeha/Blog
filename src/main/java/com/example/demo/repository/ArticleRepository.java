package com.example.demo.repository;

import com.example.demo.entity.Article;
import com.example.demo.entity.User;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface ArticleRepository extends JpaRepository<Article,Integer> {

    List<Article> findAllByUser(User user);

    @Query("SELECT a FROM Article a WHERE (a.title = :title or :title is null) " +
            "and (a.user.id = :author or :author is null)")
    List<Article> findAllByTitleAndAuthor(@Param("title") String title,
                                          @Param("author") Integer author,
                                          Sort sort);

    @Query("SELECT a FROM Article a WHERE (a.title = :title or :title is null) " +
            "and (a.user.id = :author or :author is null) and a.status = com." +
            "example.demo.enumeration.Status.PUBLIC")
    List<Article> findAllPublicByTitleAndAuthor(@Param("title") String title,
                                                @Param("author") Integer author,
                                                Sort sort);
}
