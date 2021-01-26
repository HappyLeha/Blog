package com.example.demo.repository;

import com.example.demo.entity.ResetCode;
import com.example.demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User,Integer> {
    boolean existsByEmail(String email);
    boolean existsByEmailAndEnabled(String email,boolean enabled);
    User findByEmail(String email);
}
