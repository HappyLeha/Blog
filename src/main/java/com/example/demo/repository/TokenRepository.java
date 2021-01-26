package com.example.demo.repository;

import com.example.demo.entity.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<VerificationToken,Integer> {
   Optional<VerificationToken> findFirstByToken(String token);
   boolean existsByToken(String token);
   void deleteAllByExpiryDateIsLessThanEqual(Date date);
}
