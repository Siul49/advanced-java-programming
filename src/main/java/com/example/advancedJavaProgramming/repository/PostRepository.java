package com.example.advancedJavaProgramming.repository;

import com.example.advancedJavaProgramming.model.Post;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends MongoRepository<Post, String> {
    // 기본적인 CRUD 메서드 사용 가능
}
