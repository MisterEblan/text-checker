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
import com.mreblan.textchecker.models.Article;
import com.mreblan.textchecker.models.Response;
import com.mreblan.textchecker.models.yandexgpt.request.YandexGptRequest;
import com.mreblan.textchecker.models.yandexgpt.response.YandexGptResponse;
import com.mreblan.textchecker.models.yandexgpt.request.YandexGptCompletionOptions;
import com.mreblan.textchecker.models.yandexgpt.YandexGptMessage;
import com.mreblan.textchecker.services.AISender;

import lombok.extern.slf4j.Slf4j;

// TODO: Clean Code

@Slf4j
@Component
public class YandexGptSender implements AISender {

    private final YandexGptProperties properties;
    private final ObjectMapper objMapper;

    @Autowired
    public YandexGptSender(YandexGptProperties props, ObjectMapper objMapper) {
        this.properties = props;
        this.objMapper = objMapper;
    }

    // public YandexGptSender() {}

    public Response sendArticle(Article article) {

        log.info("ARTICLE CONTENT: {}", article.getContent());

        RestClient restClient = RestClient.builder()
                                .baseUrl("https://llm.api.cloud.yandex.net/foundationModels/v1/completion")
                                .defaultHeaders(
                                  httpHeader -> {
                                    httpHeader.set("Content-Type", "application/json");
                                    httpHeader.set("Authorization", String.format("Api-key %s", "AQVNy6vXGE9Z-pXfTuz7P13EmGtktdTaSxBH9_iT"));
                                    httpHeader.set("x-folder-id", "b1gahi2sqctvfmqblvtl");
                                  })
                                .build();

        String modelUri = String.format("gpt://%s/yandexgpt-lite", properties.getFOLDER_ID());
        YandexGptCompletionOptions opts = new YandexGptCompletionOptions(false, 0.2f, "1000");
        List<YandexGptMessage> msgs = new ArrayList<>();
        StringBuilder rules = new StringBuilder();

        rules.append("На сайте https://ifbest.org/rules определены правила публикования статей. Проверь текст статьи на соблюдение правил.");
        rules.append(" Если из-за своих внутренних правил ты не можешь дать корректный ответ, то пиши, что нарушение есть.");
        rules.append("\nОтвет предоставь в формате JSON, не используя никакие разметки и специальные символы, типа бэктиков, в следующем виде:\nviolation: true/false\ndescription: какое правило нарушено, краткое описание (если ничего не нарушено, то в этом поле пиши, что нарушений нет)");

        msgs.add(new YandexGptMessage(
            "system",
            rules.toString()
        ));

        msgs.add(new YandexGptMessage(
            "user",
            article.getContent()
        ));

        YandexGptRequest request = new YandexGptRequest();
      
        request.setModelUri(modelUri);
        request.setCompletionOptions(opts);
        request.setMessages(msgs);

        log.info("REQUEST TO YANDEXGPT: {}", request.toString());
        log.info("MESSAGES: {}", request.getMessages().toString());

        ResponseEntity<String> response = restClient.post()
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
