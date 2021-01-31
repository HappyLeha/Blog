package com.example.demo.entity;

import com.example.demo.enumeration.Status;
import lombok.*;
import javax.persistence.*;
import java.util.Date;
import java.util.List;

@EqualsAndHashCode
@ToString
@Getter
@Entity
@Table(name = "articles")
@NoArgsConstructor
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Setter
    @Column(name = "title")
    private String title;

    @Setter
    @Column(name = "text")
    private String text;

    @Setter
    @Column(name = "status")
    private Status status;

    @Column(name = "created_at")
    private Date createdAt;

    @Setter
    @Column(name = "updated_at")
    private Date updatedAt;

    @ManyToOne
    @JoinColumn(name = "author_id")
    @Setter
    private User user;

    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<Comment> comments;

    @Setter
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "tags_articles",
            joinColumns = @JoinColumn(name = "articles_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private List<Tag> tags;

    public Article(String title, String text, Status status, User user,
                   List<Tag> tags) {
        this.title = title;
        this.text = text;
        this.status = status;
        this.user = user;
        this.createdAt = new Date();
        this.updatedAt = this.createdAt;
        this.tags = tags;
    }
}
