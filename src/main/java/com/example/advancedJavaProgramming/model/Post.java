package com.example.advancedJavaProgramming.model;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "posts")
public class Post {
    @Id
    private String id;

    private String title;
    private String category;
    private String content;
    private String author;
    private String image;

    private Date createdAt;
}
