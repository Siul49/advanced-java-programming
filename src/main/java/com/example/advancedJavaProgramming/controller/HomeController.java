package com.example.advancedJavaProgramming.controller;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("title", "공공재 메인페이지");
        model.addAttribute("currentPage", "home");
        return "index";
    }

    @GetMapping("/team-recruit")
    public String teamRecruit(Model model) {
        model.addAttribute("title", "<UNK> <UNK>");
        model.addAttribute("currentPage", "team-recruit");
        return "team-recruit";
    }

    @GetMapping("/team-recruit-detail")
    public String teamRecruitDetail(Model model) {
        model.addAttribute("title", "<UNK> <UNK>");
        model.addAttribute("currentPage", "team-recruit-detail");
        return "team-recruit-detail";
    }

    @GetMapping("/main")
    public String mainPage(Model model) {
        model.addAttribute("currentPage", "home"); // 현재 페이지 식별자
        model.addAttribute("title", "공공재 메인");
        return "index";
    }
}
