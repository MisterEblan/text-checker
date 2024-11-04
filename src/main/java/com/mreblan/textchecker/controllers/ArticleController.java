package com.mreblan.textchecker.controllers;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mreblan.textchecker.models.Response;

import lombok.extern.slf4j.Slf4j;

import com.mreblan.textchecker.models.Article;

@Slf4j
@RestController
@RequestMapping("/api/articles")
public class ArticleController {

    @PostMapping
    public Response getArticle(@RequestBody Article article) {
        log.info(article.toString());
        return new Response(false, "No");
    }
}
