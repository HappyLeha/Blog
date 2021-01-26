package com.example.demo.repository;

import com.example.demo.entity.ResetCode;
import com.example.demo.entity.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface CodeRepository extends JpaRepository<ResetCode,Integer> {
    boolean existsByCode(String code);
    ResetCode findByCode(String code);
    void deleteAllByExpiryDateIsLessThanEqual(Date date);
}
