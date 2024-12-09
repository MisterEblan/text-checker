package com.mreblan.textchecker.services.impl;

import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mreblan.textchecker.exceptions.IllegalGptTypeException;
import com.mreblan.textchecker.models.Article;
import com.mreblan.textchecker.models.GptType;
import com.mreblan.textchecker.services.IAiSender;
import com.mreblan.textchecker.services.IArticleService;
import com.mreblan.textchecker.parsers.IParser;
import com.mreblan.textchecker.parsers.impl.HTMLParser;
import com.mreblan.textchecker.models.Response;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class ArticleServiceImpl implements IArticleService {
    private final IParser htmlParser;
    private final IAiSender yandexGptSender;
	private final IAiSender openAiService;

	@Override
	public Response processArticle(Article article, GptType gptType) throws IllegalGptTypeException {
		Article cleanedArticle = htmlParser.deleteTags(article);
		// Дополнительно логируем очищенный контент
		// Чтобы убедиться, что всё прошло нормально
		log.info("CLEANED ARTICLE: {}", cleanedArticle.toString());

		switch (gptType) {
			case YANDEX:
				return yandexGptSender.sendArticle(cleanedArticle);
			case OPEN_AI:
				return openAiService.sendArticle(cleanedArticle);
			default:
				throw new IllegalGptTypeException("Illegal GPT TYPE!");
		} 
	}
    
	//@Override
	//public Response yandexProcessArticle(Article article) {
	//	Article cleanedArtical = htmlParser.deleteTags(article);
	//	// Дополнительно логируем очищенный контент
	//	// Чтобы убедиться, что всё прошло нормально
	//	log.info("CLEANED ARTICLE: {}", cleanedArtical.toString());
	//
	//	// Передаём очищенный контент
	//	// в нейросеть и получаем оттуда ответ
	//	return yandexGptSender.sendArticle(cleanedArtical);
	//}
	//
	//@Override
	//public Response openAiProcessArticle(Article article) throws BadRequestException {
	//	Article cleanedArticle = htmlParser.deleteTags(article);
	//
	//	log.info("CLEANED ARTICLE: {}", cleanedArticle.toString());
	//
	//	return openAiService.sendArticle(cleanedArticle);
	//}
}
