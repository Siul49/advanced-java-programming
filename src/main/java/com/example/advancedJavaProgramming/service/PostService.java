package com.example.advancedJavaProgramming.service;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import com.example.advancedJavaProgramming.model.Post;
import com.example.advancedJavaProgramming.repository.PostRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class PostService {

    private PostRepository postRepository;

    // 글 저장 (업로드된 이미지 파일명도 받음)
    public Post savePost(String title, String category, String content, String imageFileName) {
        Post post = new Post();
        post.setTitle(title);
        post.setCategory(category);
        post.setContent(content);
        post.setAuthor("익명"); // 임시로 익명 설정
        post.setImage(imageFileName != null ? "/uploads/" + imageFileName : null);
        post.setCreatedAt(new Date());

        return postRepository.save(post);
    }

    // 글 목록 (페이징)
    public List<Post> getPosts(int page, int size) {
        return postRepository.findAll(PageRequest.of(page, size)).getContent();
    }

    // 전체 글 수
    public long getTotalPostCount() {
        return postRepository.count();
    }

    // 글 상세 조회
    public Optional<Post> getPostById(String id) {
        return postRepository.findById(id);
    }
}

