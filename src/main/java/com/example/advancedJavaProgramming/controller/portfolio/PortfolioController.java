package com.example.advancedJavaProgramming.controller.portfolio;

import com.example.advancedJavaProgramming.model.Portfolio;
import com.example.advancedJavaProgramming.repository.UserRepository;
import com.example.advancedJavaProgramming.service.FileStorageService;
import com.example.advancedJavaProgramming.service.PortfolioService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Controller
public class PortfolioController {

    private final PortfolioService portfolioService;
    private final FileStorageService fileStorageService;

    private UserRepository userRepository;

    public PortfolioController(PortfolioService portfolioService, FileStorageService fileStorageService) {
        this.portfolioService = portfolioService;
        this.fileStorageService = fileStorageService;
    }

    @GetMapping("/portfolio")
    public String getPortfolioPage(Principal principal, Model model) {
        String email = principal.getName();   // email

        Portfolio portfolio = portfolioService.getPortfolioByEmail(email);

        System.out.println("what are you : " + principal.getName());
        System.out.println("portfolio : " + portfolio);

        if (portfolio == null) {
            return "redirect:/writePortfolio";
        }

        model.addAttribute("name", portfolio.getName());
        model.addAttribute("about", portfolio.getAbout());
        model.addAttribute("profileImage", portfolio.getProfileImage());
        model.addAttribute("projects", portfolio.getProjects());
        model.addAttribute("skills", portfolio.getSkills());
        model.addAttribute("userId", portfolio.getUserId());

        return "portfolio";
    }


    @GetMapping("/writePortfolio")
    public String writePortfolioPage(Model model, Authentication authentication) {
        // 기본값 넘겨주기
        model.addAttribute("userId", authentication.getName());
        model.addAttribute("name", "");
        model.addAttribute("about", "");
        model.addAttribute("profileImage", "");
        model.addAttribute("projects", null);
        model.addAttribute("skills", null);

        return "writePortfolio";
    }

    @GetMapping("/editPortfolio")
    public String getEditPortfolioPage(@RequestParam("id") String userId, Model model) {
        Portfolio portfolio = portfolioService.getPortfolioByUserId(userId);

        model.addAttribute("name", portfolio.getName());
        model.addAttribute("about", portfolio.getAbout());
        model.addAttribute("profileImage", portfolio.getProfileImage());
        model.addAttribute("projects", portfolio.getProjects());
        model.addAttribute("skills", portfolio.getSkills());

        model.addAttribute("userId", userId); // id도 넘겨줘야 함!
        return "writePortfolio"; // Thymeleaf 템플릿 이름
    }

    @PostMapping(value = "/savePortfolio", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> savePortfolio(
            Principal principal, // 여기
            @RequestParam("id") String userId,
            @RequestParam("name") String name,
            @RequestParam("about") String about,
            @RequestParam("skills") String skillsJson,
            @RequestParam("projects") String projectsJson,
            @RequestParam(value = "profileImage", required = false) MultipartFile profileImageFile
    ) {

        try {
            System.out.println("▶️ userId: " + userId);
            System.out.println("▶️ name: " + name);
            System.out.println("▶️ about: " + about);
            System.out.println("▶️ skillsJson: " + skillsJson);
            System.out.println("▶️ projectsJson: " + projectsJson);

            // 1. JSON 파싱
            ObjectMapper objectMapper = new ObjectMapper();
            List<Portfolio.Skill> skills = objectMapper.readValue(skillsJson, new TypeReference<>() {});
            List<Portfolio.Project> projects = objectMapper.readValue(projectsJson, new TypeReference<>() {});

            // 2. 파일 처리
            String profileImagePath = null;
            if (profileImageFile != null && !profileImageFile.isEmpty()) {
                profileImagePath =fileStorageService.storeFile(profileImageFile);
            }

            // 3. 포트폴리오 객체 생성 및 저장
            Portfolio portfolio = Portfolio.builder()
                    .email(principal.getName())
                    .name(name)
                    .about(about)
                    .skills(skills)
                    .projects(projects)
                    .profileImage(profileImagePath)
                    .updatedAt(new Date())
                    .build();

            portfolioService.upsertPortfolio(portfolio);

            // 4. 성공 응답
            return ResponseEntity.ok().body(Map.of(
                    "status", "success",
                    "redirectUrl", "/portfolio"
            ));

        } catch (JsonProcessingException e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "error", "JSON 파싱 오류",
                    "message", e.getMessage()
            ));
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body(Map.of(
                    "error", "파일 저장 실패",
                    "message", e.getMessage()
            ));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of(
                    "error", "서버 오류",
                    "message", e.getMessage()
            ));
        }
    }
}
