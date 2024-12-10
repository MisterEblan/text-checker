package com.mreblan.textchecker.services.impl;

import java.util.Map;

import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mreblan.textchecker.config.YandexGptProperties;
import com.mreblan.textchecker.exceptions.BadResponseException;
import com.mreblan.textchecker.factories.IGptRequestFactory;
import com.mreblan.textchecker.factories.impl.YandexGptRequestFactoryImpl;
import com.mreblan.textchecker.models.Article;
import com.mreblan.textchecker.models.Response;
import com.mreblan.textchecker.models.yandexgpt.request.YandexGptRequest;
import com.mreblan.textchecker.models.yandexgpt.response.YandexGptResponse;
import com.mreblan.textchecker.models.yandexgpt.YandexGptMessage;
import com.mreblan.textchecker.services.IAiSender;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

// TODO: Clean Code

@Slf4j
@RequiredArgsConstructor
@Component
public class YandexGptService implements IAiSender {

    private final IGptRequestFactory<YandexGptRequest> yandexRequestFactory;
    private final RestClient restClient;

    @Override
    public String sendArticle(Article article) throws BadResponseException {

        // Логируем пришедшую статью
        log.info("ARTICLE CONTENT: {}", article.getContent());

        // Создаём запрос
        YandexGptRequest request = yandexRequestFactory.createRequest(article);
        
        // Логируем запрос
        log.info("REQUEST TO YANDEXGPT: {}", request.toString());
        log.info("MESSAGES: {}", request.getMessages().toString());

        // Отправляем запрос и получаем ответ, сохраняем в response
        ResponseEntity<YandexGptResponse> response = restClient.post().uri(YandexGptProperties.URI)
												  .header("Authorization", "Api-key %s".formatted(YandexGptProperties.API_KEY))
                                                  .header("x-folder-id", YandexGptProperties.FOLDER_ID)
                                                  .contentType(MediaType.APPLICATION_JSON)
                                                  .body(request)
                                                  .retrieve()
												  .onStatus(HttpStatusCode::is4xxClientError, (_request, _response) -> {
														log.error("BAD REQUEST TO YANDEX");
														log.error(_response.getStatusText());
														throw new BadResponseException("Something went wrong");
											      })
                                                  .toEntity(YandexGptResponse.class);


        // Логируем ответ
        log.info("YANDEXGPT RESPONSE: {}", response.toString());

		YandexGptMessage responseMessage = response.getBody().getResult().getAlternatives().get(0).getMessage();

		return responseMessage.getText();
    } 
}
