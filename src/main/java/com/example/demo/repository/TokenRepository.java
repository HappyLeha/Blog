package com.example.demo.repository;

import com.example.demo.entity.VerificationToken;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TokenRepository extends CrudRepository<VerificationToken,Integer> {}
