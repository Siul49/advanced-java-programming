package com.example.advancedJavaProgramming.DTO;

import com.example.advancedJavaProgramming.model.Portfolio;

import java.util.List;

public class PortfolioRequest {
    private String id;
    private String name;
    private String about;
    private List<Portfolio.Skill> skills; // 구조 일치 필수!
    private List<Portfolio.Project> projects;
}

