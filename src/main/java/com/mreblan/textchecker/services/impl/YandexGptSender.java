package com.mreblan.textchecker.services.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mreblan.textchecker.ai.YandexGptProperties;
import com.mreblan.textchecker.factories.YandexGptRequestFactory;
import com.mreblan.textchecker.factories.impl.YandexGptRequestFactoryImpl;
import com.mreblan.textchecker.models.Article;
import com.mreblan.textchecker.models.Response;
import com.mreblan.textchecker.models.yandexgpt.request.YandexGptRequest;
import com.mreblan.textchecker.models.yandexgpt.response.YandexGptResponse;
import com.mreblan.textchecker.models.yandexgpt.request.YandexGptCompletionOptions;
import com.mreblan.textchecker.models.yandexgpt.YandexGptMessage;
import com.mreblan.textchecker.services.AISender;

import lombok.Value;
import lombok.extern.slf4j.Slf4j;

// TODO: Clean Code

@Slf4j
@Component
public class YandexGptSender implements AISender {

    private final YandexGptProperties properties;
    private final ObjectMapper objMapper;
    private YandexGptRequestFactory requestFactory;

    @Autowired
    public YandexGptSender(YandexGptProperties props, ObjectMapper objMapper, YandexGptRequestFactoryImpl requestFactory) {
        this.properties = props;
        this.objMapper = objMapper;
        this.requestFactory = requestFactory;
    }

    @Override
    public Response sendArticle(Article article) {

        log.info("ARTICLE CONTENT: {}", article.getContent());

        RestClient restClient = RestClient.builder()
                                .baseUrl("https://llm.api.cloud.yandex.net/foundationModels/v1/completion")
                                .defaultHeaders(
                                  httpHeader -> {
                                    httpHeader.set("Content-Type", "application/json");
                                    httpHeader.set("x-folder-id", "b1gahi2sqctvfmqblvtl");
                                  })
                                .build();

        YandexGptRequest request = requestFactory.createRequest(article);
        
        log.info("REQUEST TO YANDEXGPT: {}", request.toString());
        log.info("MESSAGES: {}", request.getMessages().toString());

        ResponseEntity<String> response = restClient.post().header("Authorization", String.format("Api-key %s", properties.getAPI_KEY()))
                                                  .contentType(MediaType.APPLICATION_JSON)
                                                  .body(request)
                                                  .retrieve()
                                                  .toEntity(String.class);


        log.info("YANDEXGPT RESPONSE: {}", response.toString());

        YandexGptMessage responseMessage = new YandexGptMessage(null, null);
        Response finalResponse = new Response();

        try {
            responseMessage = processAiResponse(response);
            finalResponse   = responseMaker(responseMessage.getText());
        } catch (JsonProcessingException e) {
            log.error("ERROR WITH PROCESSING JSON!");
            e.printStackTrace();
        }

        log.info("YANDEXGPT RESPONSE MESSAGE: {}", responseMessage.toString());
        log.info("FINAL RESPONSE: {}", finalResponse.toString());

        return finalResponse;
    } 

    private YandexGptMessage processAiResponse(ResponseEntity<String> response) throws JsonProcessingException {
        YandexGptResponse AiResponse =  objMapper.readValue(response.getBody(), YandexGptResponse.class);

        if (
            AiResponse != null &&
            AiResponse.getResult() != null &&
            !AiResponse.getResult().getAlternatives().isEmpty()
        ) {
            String role = AiResponse.getResult().getAlternatives().get(0).getMessage().getRole();
            String text = AiResponse.getResult().getAlternatives().get(0).getMessage().getText();

            return new YandexGptMessage(role, text);
        }   

        return new YandexGptMessage(null, null);
    }

    private Response responseMaker(String jsonText) throws JsonProcessingException {
        jsonText = jsonText.replace("`", "");
        return objMapper.readValue(jsonText, Response.class);
    }
}
