package com.example.advancedJavaProgramming.service;


import com.example.advancedJavaProgramming.model.Portfolio;
import com.example.advancedJavaProgramming.repository.PortfolioRepository;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class PortfolioService {

    private final PortfolioRepository portfolioRepository;

    public PortfolioService(PortfolioRepository portfolioRepository) {
        this.portfolioRepository = portfolioRepository;
    }

    public Portfolio getPortfolioByUserId(String userId) {
        return portfolioRepository.findByUserId(userId);
    }

    public Portfolio getPortfolioByEmail(String email) {
        return portfolioRepository.findByEmail(email);
    }

    public void upsertPortfolio(Portfolio portfolio) {
        // MongoDB에서 사용자 ID로 기존 포트폴리오 찾기
        Portfolio existing = portfolioRepository.findByUserId(portfolio.getUserId());

        if (existing != null) {
            // 업데이트
            existing.setName(portfolio.getName());
            existing.setAbout(portfolio.getAbout());
            existing.setSkills(portfolio.getSkills());
            existing.setProjects(portfolio.getProjects());
            existing.setUpdatedAt(new Date());
            if (portfolio.getProfileImage() != null) {
                existing.setProfileImage(portfolio.getProfileImage());
            }
            portfolioRepository.save(existing);
        } else {
            // 새로 삽입
            portfolio.setCreatedAt(new Date());
            portfolioRepository.save(portfolio);
        }
    }
}
