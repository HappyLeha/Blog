package com.example.demo.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

import java.util.Calendar;
import java.util.Date;

@EqualsAndHashCode
@ToString
@Getter
@Entity
@Table(name = "tokens")
@NoArgsConstructor
public class VerificationToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String token;
    @OneToOne
    @JoinColumn(nullable = false, name = "user_id")
    private User user;
    private Date expiryDate;

    public VerificationToken(String token, User user, Date expiryDate) {
        this.token = token;
        this.user = user;
        this.expiryDate = expiryDate;
    }

    private Date calculateExpiryDate(int expiryTimeInMinutes) {
        return null;
    }
}
