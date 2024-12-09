package com.mreblan.textchecker.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mreblan.textchecker.models.Response;
import com.mreblan.textchecker.services.IArticleService;
import com.mreblan.textchecker.services.impl.ArticleServiceImpl;

import lombok.extern.slf4j.Slf4j;

import com.mreblan.textchecker.models.Article;
import com.mreblan.textchecker.models.ErrorResponse;

@Slf4j
@RestController
@RequestMapping("/api/articles")
public class ArticleController {

    private final IArticleService service;

    @Autowired
    public ArticleController(ArticleServiceImpl serviceImpl) {
        this.service = serviceImpl;
    }

    @PostMapping("/yandex")
    public Response yandexCheckArticle(@RequestBody Article article) {
        log.info(article.toString());

        return service.yandexProcessArticle(article);
    }

    @PostMapping("/openai")
    public ResponseEntity<?> openAiCheckArticle(@RequestBody Article article) {
        log.info(article.toString());

		Response response = service.openAiProcessArticle(article);

		if (response == null) {
			ErrorResponse errResponse = ErrorResponse.builder()
											.success(false)
											.message("Что-то пошло не так")
											.build();

			return ResponseEntity
						.status(HttpStatus.INTERNAL_SERVER_ERROR)
						.body(errResponse);
		}

		return ResponseEntity.ok(response);

    }
}
