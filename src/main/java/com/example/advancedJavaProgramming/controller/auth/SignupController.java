package com.example.advancedJavaProgramming.controller.auth;

import com.example.advancedJavaProgramming.model.User;
import com.example.advancedJavaProgramming.repository.UserRepository;
import com.mongodb.DuplicateKeyException;
import jakarta.validation.Valid;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
            Model model,
            RedirectAttributes redirectAttributes) {

        // 1. 비밀번호 일치 검증
        if (!user.getPassword().equals(confirmPassword)) {
            bindingResult.rejectValue("password", "password.mismatch", "비밀번호가 일치하지 않습니다");
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("title", "공공재 회원가입");
            return "signup";
        }

        // 2. 비밀번호 암호화 (반드시 저장 전에!)
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // 3. DB 저장 (한 번만 실행)
        try {
            userRepository.save(user);
        } catch (DuplicateKeyException ex) {
            bindingResult.rejectValue("email", "duplicate.email", "이미 사용 중인 이메일입니다.");
            return "signup";
        }

        // 4. 리다이렉트 속성 설정
        redirectAttributes.addFlashAttribute("name", user.getName());
        redirectAttributes.addFlashAttribute("email", user.getEmail());

        return "redirect:/signup-success";
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

    @GetMapping("/signup-success")
    public String signupSuccess(Model model) {
        model.addAttribute("title", "회원가입 완료");
        return "signup-success";
    }
}
