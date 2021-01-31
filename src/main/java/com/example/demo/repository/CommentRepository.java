package com.example.demo.repository;

import com.example.demo.entity.Comment;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface CommentRepository extends JpaRepository<Comment,Integer> {

    @Query("SELECT c FROM Comment c WHERE (c.article.title = :title or " +
            ":title is null) and (c.user.id = :author or :author is null)")
    List<Comment> findAllByTitleAndAuthor(@Param("title") String title,
                                          @Param("author") Integer author,
                                          Sort sort);
}
