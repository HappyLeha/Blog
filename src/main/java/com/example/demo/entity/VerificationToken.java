package com.example.demo.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import javax.persistence.*;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

@EqualsAndHashCode
@ToString
@Getter
@Entity
@Table(name = "tokens")
@NoArgsConstructor
public class VerificationToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "token")
    private String token;

    @Column(name = "expiry_date")
    private Date expiryDate;

    @OneToOne
    @JoinColumn(nullable = false, name = "user_id")
    private User user;

    public VerificationToken(User user) {
        Calendar calendar = Calendar.getInstance();

        this.user = user;
        this.token = UUID.randomUUID().toString();
        calendar.add(Calendar.DAY_OF_YEAR,1);
        this.expiryDate = calendar.getTime();
    }
}
