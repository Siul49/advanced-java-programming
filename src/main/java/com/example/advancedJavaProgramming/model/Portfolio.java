package com.example.advancedJavaProgramming.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@Builder
@Document(collection = "user_portfolio")
public class Portfolio {

    @Id
    private String _id;
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
        public String title;
        public String description;
    }

    @Setter
    @Getter
    public static class Skill {
        public String name;
        public String color;
    }
}
