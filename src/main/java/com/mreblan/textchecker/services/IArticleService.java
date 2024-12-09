package com.mreblan.textchecker.services;

import com.mreblan.textchecker.models.Response;

import org.apache.coyote.BadRequestException;

import com.mreblan.textchecker.models.Article;

public interface IArticleService {
    
    Response yandexProcessArticle(Article article);
	Response openAiProcessArticle(Article article) throws BadRequestException;
}
