package com.example.demo.repository;

import com.example.demo.entity.ResetCode;
import com.example.demo.entity.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface TokenRepository extends
        JpaRepository<VerificationToken,Integer> {

   Optional<VerificationToken> findFirstByToken(String token);

   void removeByExpiryDateLessThan(Date date);

   List<VerificationToken> findAllByExpiryDateLessThan(Date date);
}
