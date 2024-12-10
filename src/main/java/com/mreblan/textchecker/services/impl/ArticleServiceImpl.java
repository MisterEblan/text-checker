package com.mreblan.textchecker.services.impl;

import java.util.Map;

import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mreblan.textchecker.exceptions.BadResponseException;
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
    private final IAiSender yandexGptService;
	private final IAiSender openAiService;
	private final ObjectMapper objMapper;

	@Override
	public Response processArticle(Article article, GptType gptType) throws IllegalGptTypeException {
		Article cleanedArticle = htmlParser.deleteTags(article);
		// Дополнительно логируем очищенный контент
		// Чтобы убедиться, что всё прошло нормально
		log.info("CLEANED ARTICLE: {}", cleanedArticle.toString());

		switch (gptType) {
			case YANDEX:
				try {
					String json = yandexGptService.sendArticle(cleanedArticle);
					return jsonToResponse(json);
				} catch (BadResponseException e) {
					log.error(e.getMessage());
					return null;
				} catch (JsonProcessingException e) {
					log.error(e.getMessage());
					return Response.builder()
								.isViolated(true)
								.description("Нейросеть не смогла обработать запрос")
								.build();
				}
				
			case OPEN_AI:
				try {
					String json = openAiService.sendArticle(cleanedArticle);
					return jsonToResponse(json);
				} catch (BadResponseException e) {
					log.error(e.getMessage());
					return null;
				} catch (JsonProcessingException e) {
					log.error(e.getMessage());
					return Response.builder()
								.isViolated(true)
								.description("Нейросеть не смогла обработать запрос")
								.build();
				}
			default:
				throw new IllegalGptTypeException("Illegal GPT TYPE!");
		} 
	}
    
    private Response jsonToResponse(String jsonText) throws JsonProcessingException {
        // Чистим текст от ненужных символов
        jsonText = jsonText.replace("`", "");

        // Создаём временную мапу, где будут содержатся значения
        Map<String, Object> tempMap = objMapper.readValue(jsonText, new TypeReference<Map<String, Object>>() {});

        // Возвращаем новый ответ
		return Response.builder()
					.isViolated((Boolean) tempMap.get("isViolated"))
					.description((String) tempMap.get("description"))
					.build();
    }
}
