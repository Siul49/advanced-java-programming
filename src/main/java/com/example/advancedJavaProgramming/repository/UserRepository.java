package com.example.advancedJavaProgramming.repository;


import com.example.advancedJavaProgramming.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String> {
    boolean existsByNickname(String nickname);
    boolean existsByEmail(String email);
    boolean existsByAccountId(String accountId);
    User findByEmail(String email);

}
