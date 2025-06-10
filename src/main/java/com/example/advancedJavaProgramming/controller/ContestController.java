package com.example.advancedJavaProgramming.controller;

import com.example.advancedJavaProgramming.service.ContestService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Controller
public class ContestController {

    @Autowired
    private ContestService contestService;

    // 메인 페이지
    @GetMapping("/mainPage")
    public String mainPage(Model model) {
        List<Map<String, Object>> recommendedContests = contestService.getRecommendedContests();
        List<Map<String, Object>> contests = contestService.getAllContests();
        Set<String> allKeywords = contestService.getAllKeywords();

        // 예시: 사용자별 키워드 (아직 미구현 → 임시 데이터)
        List<String> userKeywords = List.of("영상/UCC/사진", "광고/마케팅");

        model.addAttribute("recommendedContests", recommendedContests);
        model.addAttribute("contests", contests);
        model.addAttribute("userKeywords", userKeywords);
        model.addAttribute("allKeywords", allKeywords);

        return "mainPage";
    }

    // AJAX: 필터링
    @GetMapping("/contest/filter")
    @ResponseBody
    public List<Map<String, Object>> filterContests(@RequestParam String filter,
                                                    @RequestParam(required = false) String keyword) {
        return contestService.filterContests(filter, keyword);
    }
}
