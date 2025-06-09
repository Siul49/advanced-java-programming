package com.example.advancedJavaProgramming.controller;

import com.example.advancedJavaProgramming.model.User;
import com.example.advancedJavaProgramming.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
public class SignupController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // 생성자 주입
    public SignupController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/signup")
    public String signupForm(Model model) {
        model.addAttribute("title", "공공재 회원가입");
        return "signup";
    }

    @PostMapping("/signup")
    public String signupSubmit(
            @Valid @ModelAttribute User user,
            @RequestParam("confirm-password") String confirmPassword,
            BindingResult bindingResult,
            Model model  // Model 추가
    ) {
        // 비밀번호 일치 검증
        if (!user.getPassword().equals(confirmPassword)) {
            bindingResult.rejectValue("password", "password.mismatch", "비밀번호가 일치하지 않습니다");
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("title", "공공재 회원가입");  // 타이틀 다시 추가
            return "signup";
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return "redirect:/SignupSuccess";
    }

    @GetMapping("/check-nickname")
    @ResponseBody
    public boolean checkNickname(@RequestParam String nickname) {
        return userRepository.existsByNickname(nickname);
    }

    @GetMapping("/check-email")
    @ResponseBody
    public boolean checkEmail(@RequestParam String email) {
        return userRepository.existsByEmail(email);
    }
}
