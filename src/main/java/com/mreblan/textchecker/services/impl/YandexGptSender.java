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
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mreblan.textchecker.ai.YandexGptProperties;
import com.mreblan.textchecker.factories.IYandexGptRequestFactory;
import com.mreblan.textchecker.factories.impl.YandexGptRequestFactoryImpl;
import com.mreblan.textchecker.models.Article;
import com.mreblan.textchecker.models.Response;
import com.mreblan.textchecker.models.yandexgpt.request.YandexGptRequest;
import com.mreblan.textchecker.models.yandexgpt.response.YandexGptResponse;
import com.mreblan.textchecker.models.yandexgpt.request.YandexGptCompletionOptions;
import com.mreblan.textchecker.models.yandexgpt.YandexGptMessage;
import com.mreblan.textchecker.services.IAiSender;

import lombok.Value;
import lombok.extern.slf4j.Slf4j;

// TODO: Clean Code

@Slf4j
@Component
public class YandexGptSender implements IAiSender {

    private final YandexGptProperties properties;
    private final ObjectMapper objMapper;
    private IYandexGptRequestFactory requestFactory;

    @Autowired
    public YandexGptSender(YandexGptProperties props, ObjectMapper objMapper, YandexGptRequestFactoryImpl requestFactory) {
        this.properties = props;
        this.objMapper = objMapper;
        this.requestFactory = requestFactory;
    }

    @Override
    public Response sendArticle(Article article) throws RuntimeException {

        // Логируем пришедшую статью
        log.info("ARTICLE CONTENT: {}", article.getContent());

        // Создаём RestClient для отправки запроса
        RestClient restClient = RestClient.builder()
                                .baseUrl("https://llm.api.cloud.yandex.net/foundationModels/v1/completion")
                                .defaultHeaders(
                                  httpHeader -> {
                                    httpHeader.set("Content-Type", "application/json");
                                  })
                                .build();

        // Создаём запрос
        YandexGptRequest request = requestFactory.createRequest(article);
        
        // Логируем запрос
        log.info("REQUEST TO YANDEXGPT: {}", request.getCompletionOptions().getTemperature());
        log.info("MESSAGES: {}", request.getMessages().toString());

        // Отправляем запрос и получаем ответ, сохраняем в response
        ResponseEntity<String> response = restClient.post().header("Authorization", String.format("Api-key %s", properties.getAPI_KEY()))
                                                  .header("x-folder-id", properties.getFOLDER_ID())
                                                  .contentType(MediaType.APPLICATION_JSON)
                                                  .body(request)
                                                  .retrieve()
                                                  .toEntity(String.class);


        // Логируем ответ
        log.info("YANDEXGPT RESPONSE: {}", response.toString());

        // Проверяем, был ли запрос удачным
        if (response.getStatusCode().is2xxSuccessful()) {

            // Делаем заготовки для обработки ответа
            YandexGptMessage responseMessage = new YandexGptMessage(null, null);
            Response finalResponse = new Response();

            try {
                // Парсим JSON-ответ, получая из него только нужные элементы
                responseMessage = processAiResponse(response);
                // Формируем финальный ответ
                finalResponse   = responseMaker(responseMessage.getText());
            } catch (JsonProcessingException e) {
                log.error("ERROR WITH PROCESSING JSON!");
                e.printStackTrace();

                // Если мы не смогли распарсить ответ,
                // значит, нейросеть не смогла его обработать,
                // значит, содержание статьи явно не соблюдает правила
                return new Response(true, "Нейросеть не смогла обработать запрос");
            }

            // Логируем ответ от YandexGPT
            log.info("YANDEXGPT RESPONSE MESSAGE: {}", responseMessage.toString());
            // Логируем финальный сформированный ответ
            log.info("FINAL RESPONSE: {}", finalResponse.toString());

            // Возвращаем ответ
            return finalResponse;
        } else {
            // Если запрос был неудачный,
            // кидаем исключение
            throw new RuntimeException("Response to request" + request.toString() + " wasn't successful!");
        }
    } 

    // Обрабатывает поступивший ответ, возвращая сообщение от нейросети
    private YandexGptMessage processAiResponse(ResponseEntity<String> response) throws JsonProcessingException {
        // Парсим весь ответ от YandexGPT с помощью Jackson
        YandexGptResponse aiResponse =  objMapper.readValue(response.getBody(), YandexGptResponse.class);

        // Проверяем, что ответ есть
        // и что нужные нам поля не пустые
        if (
            aiResponse != null &&
            aiResponse.getResult() != null &&
            !aiResponse.getResult().getAlternatives().isEmpty()
        ) {
            // Записываем нужные нам данные из ответа в переменные
            String role = aiResponse.getResult().getAlternatives().get(0).getMessage().getRole();
            String text = aiResponse.getResult().getAlternatives().get(0).getMessage().getText();

            // Возвращаем сообщение от YandexGPT
            return new YandexGptMessage(role, text);
        }   

        // В противном случае возвращаем пустое сообщение
        // TODO: Сделать проверку в главном коде
        return new YandexGptMessage(null, null);
    }

    // Создаёт итоговый ответ из текста нейросети
    private Response responseMaker(String jsonText) throws JsonProcessingException {
        // Чистим текст от ненужных символов
        jsonText = jsonText.replace("`", "");

        // Создаём временную мапу, где будут содержатся значения
        Map<String, Object> tempMap = objMapper.readValue(jsonText, new TypeReference<Map<String, Object>>() {});

        // Создаём новый ответ
        Response response = new Response();

        // Записываем полученные от нейросети значения.
        // Нужно для большей надёжности
        response.setViolated((Boolean) tempMap.get("isViolated"));
        response.setDescription((String) tempMap.get("description"));

        return response;
    }
}
