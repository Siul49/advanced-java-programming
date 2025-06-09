package com.example.advancedJavaProgramming.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@Document(collection = "user_portfolio")
public class Portfolio {

    @Id
    private String _id; // MongoDB에서 자동 생성된 ObjectId
    private String userId;

    private String name;
    private String about;
    private String profileImage;
    private String email;

    private List<Project> projects;
    private List<Skill> skills;

    private Date createdAt;
    private Date updatedAt;


    @Setter
    @Getter
    public static class Project {
        // getters/setters
        private String title;
        private String description;
    }

    // ✅ 중첩 클래스: Skill
    @Setter
    @Getter
    public static class Skill {
        private String name;
        private String color;
    }
}
