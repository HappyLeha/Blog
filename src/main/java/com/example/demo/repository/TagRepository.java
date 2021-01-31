package com.example.demo.repository;

import com.example.demo.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository extends JpaRepository<Tag,Integer> {

    boolean existsByName(String name);

    Tag findByName(String name);
}
