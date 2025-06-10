package com.example.advancedJavaProgramming.controller;

import com.example.advancedJavaProgramming.model.Contest;
import com.example.advancedJavaProgramming.repository.ContestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class ContestController {

    private final ContestRepository contestRepository;

    @Autowired
    public ContestController(ContestRepository contestRepository) {
        this.contestRepository = contestRepository;
    }

    @GetMapping("/contest")
    public String mainPage(Model model) {
        List<Contest> contests = contestRepository.findAll(); // MongoDB에서 전체 Contest 가져오기
        model.addAttribute("contests", contests); // Thymeleaf에 contests로 전달
        return "contest"; // templates/mainPage.html로 이동
    }
}
