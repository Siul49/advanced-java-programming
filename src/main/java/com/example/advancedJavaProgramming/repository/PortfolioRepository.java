package com.example.advancedJavaProgramming.repository;

import com.example.advancedJavaProgramming.model.Portfolio;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PortfolioRepository extends MongoRepository<Portfolio, String> {
    // 기본적으로 findById 제공됨
    Portfolio findByUserId(String userId);
    Portfolio findByEmail(String email);
}
