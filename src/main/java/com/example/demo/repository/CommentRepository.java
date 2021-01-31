package com.example.demo.repository;

import com.example.demo.entity.Comment;
import com.example.demo.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment,Integer> {
}
