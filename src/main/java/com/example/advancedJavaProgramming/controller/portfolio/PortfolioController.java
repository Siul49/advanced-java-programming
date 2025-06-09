package com.example.advancedJavaProgramming.controller.portfolio;

import com.example.advancedJavaProgramming.model.Portfolio;
import com.example.advancedJavaProgramming.repository.UserRepository;
import com.example.advancedJavaProgramming.service.PortfolioService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.Date;
import java.util.List;

@Controller
public class PortfolioController {

    private final PortfolioService portfolioService;

    private UserRepository userRepository;

    public PortfolioController(PortfolioService portfolioService) {
        this.portfolioService = portfolioService;
    }

    @GetMapping("/portfolio")
    public String getPortfolioPage(Principal principal, Model model) {
        String email = principal.getName();   // email

        Portfolio portfolio = portfolioService.getPortfolioByEmail(email);

        if (portfolio == null) {
            // ✅ 저장된 포트폴리오 없으면 바로 작성 페이지로 리다이렉트
            return "redirect:/writePortfolio";
        }

        // ✅ 있으면 값 채워서 포트폴리오 페이지 렌더링
        model.addAttribute("name", portfolio.getName());
        model.addAttribute("about", portfolio.getAbout());
        model.addAttribute("profileImage", portfolio.getProfileImage());
        model.addAttribute("projects", portfolio.getProjects());
        model.addAttribute("skills", portfolio.getSkills());
        model.addAttribute("userId", portfolio.getUserId());

        return "portfolio"; // Thymeleaf 템플릿 이름
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

        return "writePortfolio"; // 작성/수정 페이지 이름 (같이 쓰는 경우)
    }

    @GetMapping("/editPortfolio")
    public String getEditPortfolioPage(@RequestParam("id") String userId, Model model) {
        Portfolio portfolio = portfolioService.getPortfolioByUserId(userId);

        // ✅ 있으면 값으로 채움
        model.addAttribute("name", portfolio.getName());
        model.addAttribute("about", portfolio.getAbout());
        model.addAttribute("profileImage", portfolio.getProfileImage());
        model.addAttribute("projects", portfolio.getProjects());
        model.addAttribute("skills", portfolio.getSkills());

        model.addAttribute("userId", userId); // id도 넘겨줘야 함!
        return "writePortfolio"; // Thymeleaf 템플릿 이름
    }

    @PostMapping("/savePortfolio")
    public String savePortfolio(
            @RequestParam("id") String userId,
            @RequestParam("name") String name,
            @RequestParam("about") String about,
            @RequestParam("skills") String skillsJson,
            @RequestParam("projects") String projectsJson,
            @RequestParam(value = "profileImage", required = false) MultipartFile profileImageFile
    ) {
        try {
            // ✅ skills, projects JSON 파싱
            ObjectMapper objectMapper = new ObjectMapper();
            List<Portfolio.Skill> skills = objectMapper.readValue(skillsJson, new TypeReference<List<Portfolio.Skill>>() {});
            List<Portfolio.Project> projects = objectMapper.readValue(projectsJson, new TypeReference<List<Portfolio.Project>>() {});

            // ✅ 프로필 이미지 처리
            String profileImageFileName = null;
            if (profileImageFile != null && !profileImageFile.isEmpty()) {
                String fileName = profileImageFile.getOriginalFilename();
                // 서버 경로에 저장 (예시)
                String uploadDir = "uploads/";
                File dir = new File(uploadDir);
                if (!dir.exists()) dir.mkdirs();

                Path filePath = Paths.get(uploadDir + fileName);
                Files.copy(profileImageFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

                profileImageFileName = fileName;
            }

            // ✅ Portfolio 객체 만들기
            Portfolio portfolio = new Portfolio();
            portfolio.setUserId(userId);
            portfolio.setName(name);
            portfolio.setAbout(about);
            portfolio.setSkills(skills);
            portfolio.setProjects(projects);
            if (profileImageFileName != null) {
                portfolio.setProfileImage(profileImageFileName);

            }
            portfolio.setUpdatedAt(new Date());

            // ✅ upsert 동작
            portfolioService.upsertPortfolio(portfolio);

            // 저장 후 포트폴리오 페이지로 리다이렉트
            return "redirect:/portfolio";
        } catch (Exception e) {
            e.printStackTrace();
            return "error/500"; // 에러 페이지로
        }
    }

}
