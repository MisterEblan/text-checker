package com.mreblan.textchecker.factories.impl;

import java.util.List;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;

import com.mreblan.textchecker.ai.YandexGptProperties;
import com.mreblan.textchecker.factories.YandexGptRequestFactory;
import com.mreblan.textchecker.models.Article;
import com.mreblan.textchecker.models.yandexgpt.YandexGptMessage;
import com.mreblan.textchecker.models.yandexgpt.request.YandexGptCompletionOptions;
import com.mreblan.textchecker.models.yandexgpt.request.YandexGptRequest;

public class YandexGptRequestFactoryImpl implements YandexGptRequestFactory {

    private final YandexGptProperties properties;

    @Autowired
    public YandexGptRequestFactoryImpl(YandexGptProperties props) {
        this.properties = props;
    }
    
    @Override
    public YandexGptRequest createRequest(Article article) {
        String modelUri = String.format("gpt://%s/yandexgpt-lite", properties.getFOLDER_ID());
        YandexGptCompletionOptions opts = new YandexGptCompletionOptions(false, 0.2f, "1000");
        List<YandexGptMessage> msgs = new ArrayList<>();

        StringBuilder rules = new StringBuilder();
        rules.append("На сайте https://ifbest.org/rules определены правила публикования статей. Проверь текст статьи на соблюдение правил.");
        rules.append(" Если из-за своих внутренних правил ты не можешь дать корректный ответ, то пиши, что нарушение есть.");
        rules.append("\nОтвет предоставь в формате JSON, не используя никакие разметки и специальные символы, типа бэктиков, в следующем виде:\nisViolated: true/false\ndescription: какое правило нарушено, краткое описание (если ничего не нарушено, то в этом поле пиши, что нарушений нет)");

        msgs.add(new YandexGptMessage("system", rules.toString()));
        msgs.add(new YandexGptMessage("user", article.getTitle() + "\n" + article.getContent()));

        YandexGptRequest request = new YandexGptRequest();

        request.setModelUri(modelUri);
        request.setCompletionOptions(opts);
        request.setMessages(msgs);

        return request;
    }
    
}
