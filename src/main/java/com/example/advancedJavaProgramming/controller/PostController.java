package com.example.advancedJavaProgramming.controller;

import com.example.advancedJavaProgramming.model.Post;
import com.example.advancedJavaProgramming.service.PostService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Controller
public class PostController {

    private PostService postService;

    // 글쓰기 페이지
    @GetMapping("/community_write")
    public String showWritePage() {
        return "community_write";
    }

    // 글 저장
    @PostMapping("/community")
    public String savePost(@RequestParam("title") String title,
                           @RequestParam("category") String category,
                           @RequestParam("content") String content,
                           @RequestParam(value = "image", required = false) MultipartFile imageFile) throws IOException {
        String imageFileName = null;

        if (imageFile != null && !imageFile.isEmpty()) {
            // ✅ 파일 저장 경로
            String uploadDir = "src/main/resources/static/uploads/";
            File uploadFile = new File(uploadDir + imageFile.getOriginalFilename());
            imageFile.transferTo(uploadFile);

            imageFileName = imageFile.getOriginalFilename();
        }

        postService.savePost(title, category, content, imageFileName);

        return "redirect:/";
    }

    // 상세 페이지
    @GetMapping("/community/{id}")
    public String showPostDetail(@PathVariable String id, Model model) {
        Optional<Post> post = postService.getPostById(id);
        if (post.isPresent()) {
            model.addAttribute("post", post.get());
            return "community_detail";
        } else {
            return "redirect:/"; // 없으면 홈으로
        }
    }

    // 글 목록 (페이징)
    @GetMapping("/community")
    public String showPosts(@RequestParam(value = "page", defaultValue = "1") int page,
                            Model model) {
        int perPage = 6;
        int currentPage = page;
        long totalCount = postService.getTotalPostCount();
        int totalPages = (int) Math.ceil((double) totalCount / perPage);

        List<Post> posts = postService.getPosts(page - 1, perPage);

        model.addAttribute("posts", posts);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalPages", totalPages);

        return "community";
    }
}