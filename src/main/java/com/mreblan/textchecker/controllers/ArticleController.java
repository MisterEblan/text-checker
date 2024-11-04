package com.mreblan.textchecker.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mreblan.textchecker.models.Response;
import com.mreblan.textchecker.services.ArticleService;
import com.mreblan.textchecker.services.impl.ArticleServiceImpl;

import lombok.extern.slf4j.Slf4j;

import com.mreblan.textchecker.models.Article;

@Slf4j
@RestController
@RequestMapping("/api/articles")
public class ArticleController {

    private final ArticleService service;

    @Autowired
    public ArticleController(ArticleServiceImpl serviceImpl) {
        this.service = serviceImpl;
    }

    @PostMapping
    public Response getArticle(@RequestBody Article article) {
        log.info(article.toString());

        return service.processArticle(article);
    }
}
