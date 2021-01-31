package com.example.demo.entity;

import lombok.*;
import javax.persistence.*;
import java.util.List;

@EqualsAndHashCode
@ToString
@Getter
@Entity
@Table(name = "tags")
@NoArgsConstructor
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Setter
    @Column(name = "name")
    private String name;

    @ManyToMany
    @ToString.Exclude
    private List<Article> articles;

    public Tag(String name) {
        this.name = name;
    }
}
