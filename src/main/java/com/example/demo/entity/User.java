package com.example.demo.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@EqualsAndHashCode
@ToString
@Getter
@Entity
@Table(name = "users")
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Setter
    @Column(name = "first_name")
    private String firstName;
    @Setter
    @Column(name = "last_name")
    private String lastName;
    @ToString.Exclude
    @Setter
    @Column(name = "password")
    private String password;
    @Setter
    @Column(name = "email")
    private String email;
    @Column(name = "created_at")
    private Date createdAt;
    @Setter
    @Column(name = "enabled")
    private boolean enabled;
    @ToString.Exclude
    @OneToOne(mappedBy = "user")
    private VerificationToken token;
    @Setter
    @ToString.Exclude
    @OneToOne(mappedBy = "user")
    private ResetCode code;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<Article> articles;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<Comment> comments;

    public User(String firstName, String lastName, String password,
                String email) {
        this.enabled = false;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.email = email;
        this.createdAt = new Date();
    }
}
