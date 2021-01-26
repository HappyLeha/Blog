package com.example.demo.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

@EqualsAndHashCode
@ToString
@Getter
@Entity
@Table(name = "codes")
@NoArgsConstructor
public class ResetCode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "code")
    private String code;
    @Column(name = "expiry_date")
    private Date expiryDate;
    @OneToOne
    @JoinColumn(nullable = false, name = "user_id")
    private User user;

    public ResetCode(User user) {
        Random random=new Random();
        Calendar calendar = Calendar.getInstance();
        int length;

        this.code = String.valueOf(Math.abs((int) (random.nextDouble()*10000)));
        length = code.length();
        for (int i=0; i<4 - length; i++) {
            code = "0" + code;
        }
        calendar.add(Calendar.MINUTE,15);
        this.expiryDate = calendar.getTime();
        this.user = user;
    }
}
