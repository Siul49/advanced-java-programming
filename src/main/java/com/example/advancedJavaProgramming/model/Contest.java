package com.example.advancedJavaProgramming.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Map;

@Data
@Document(collection = "contests")
public class Contest {
    @Id
    private String id;

    private String title;
    private String imgSrc;
    private int readCount;
    private Map<String, String> info; // info.접수마감일, info.분야 등
    private int dday;
    private int prize;
    private String organizer;
}
