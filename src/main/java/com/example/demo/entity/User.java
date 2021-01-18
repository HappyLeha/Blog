package com.example.demo.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@EqualsAndHashCode
@ToString
@Getter
@Entity
@Table(name = "books")
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private int id;
    @Setter
    @Column(name="first_name")
    private String firstName;
    @Setter
    @Column(name="last_name")
    private String lastName;
    @Setter
    @Column(name="password")
    private String password;
    @Setter
    @Column(name="email")
    private String email;
    @Column(name="created_at")
    private Date createdAt;
    @Setter
    @Column(name="code")
    private int code;
    @OneToMany(mappedBy="user")
    @ToString.Exclude
    private List<Article> articles;

    public User(String firstName,String lastName,String password,String email,
                int code) {
        this.firstName=firstName;
        this.lastName=lastName;
        this.password=password;
        this.email=email;
        this.createdAt=new Date();
        this.code=code;
    }
}
