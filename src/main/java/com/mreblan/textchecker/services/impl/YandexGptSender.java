package com.mreblan.textchecker.services.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import com.mreblan.textchecker.ai.YandexGptProperties;
import com.mreblan.textchecker.models.Article;
import com.mreblan.textchecker.models.Response;
import com.mreblan.textchecker.models.yandexgpt.YandexGptRequest;
import com.mreblan.textchecker.models.yandexgpt.YandexGptCompletionOptions;
import com.mreblan.textchecker.models.yandexgpt.YandexGptMessage;
import com.mreblan.textchecker.services.AISender;

import lombok.extern.slf4j.Slf4j;

// TODO: Implement response processor
// TODO: Implement final response maker

@Slf4j
@Component
public class YandexGptSender implements AISender {

    private final YandexGptProperties properties;

    @Autowired
    public YandexGptSender(YandexGptProperties props) {
      this.properties = props;
    }

    public YandexGptSender() {}

    public Response sendArticle(Article article) {

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

        msgs.add(new YandexGptMessage(
            "system",
            "Проверь текст статьи на нарушение правил, описанных на сайте https://ifbest.org/rules.\nВерни ответ в виде:\nНарушение: есть или нет\nОписание: нарушенное правило, краткое описание."
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

        String response = restClient.post()
                                                  .contentType(MediaType.APPLICATION_JSON)
                                                  .body(request)
                                                  .retrieve()
                                                  .body(String.class);

        log.info("YANDEXGPT RESPONSE: {}", response.toString());

        return new Response(true, "rules");
    } 
}
