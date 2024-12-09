package com.mreblan.textchecker.services;

import com.mreblan.textchecker.models.Response;

import org.apache.coyote.BadRequestException;

import com.mreblan.textchecker.exceptions.IllegalGptTypeException;
import com.mreblan.textchecker.models.Article;
import com.mreblan.textchecker.models.GptType;

public interface IArticleService {
    
	//   Response yandexProcessArticle(Article article);
	//Response openAiProcessArticle(Article article) throws BadRequestException;
	Response processArticle(Article article, GptType gptType) throws IllegalGptTypeException;
}
