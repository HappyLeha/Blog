package com.example.demo.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@EqualsAndHashCode
@ToString
@Getter
@Entity
@Table(name = "comments")
@NoArgsConstructor
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Setter
    @Column(name = "message")
    private String message;
    @Column(name = "created_at")
    private Date createdAt;
    @ManyToOne
    @JoinColumn(name = "post_id")
    @Setter
    private Article article;
    @ManyToOne
    @JoinColumn(name = "author_id")
    @Setter
    private User user;


    public Comment(String message, Article article, User user) {
        this.message = message;
        this.article = article;
        this.user = user;
        this.createdAt = new Date();
    }
}
