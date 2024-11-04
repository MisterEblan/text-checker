package com.mreblan.textchecker.models;

import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
public class Article {
    private String title;
    private String content;
}
