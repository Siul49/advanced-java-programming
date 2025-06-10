package com.example.advancedJavaProgramming.repository;

import com.example.advancedJavaProgramming.model.Contest;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContestRepository extends MongoRepository<Contest, String> {

}
