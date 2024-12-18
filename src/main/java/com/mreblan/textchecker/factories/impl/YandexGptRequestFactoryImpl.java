package com.mreblan.textchecker.factories.impl;

import java.util.List;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mreblan.textchecker.config.YandexGptProperties;
import com.mreblan.textchecker.factories.IGptRequestFactory;
import com.mreblan.textchecker.models.Article;
import com.mreblan.textchecker.models.yandexgpt.YandexGptMessage;
import com.mreblan.textchecker.models.yandexgpt.request.YandexGptCompletionOptions;
import com.mreblan.textchecker.models.yandexgpt.request.YandexGptRequest;

// Фабрика, создающая запросы в YandexGPT
@Component 
public class YandexGptRequestFactoryImpl implements IGptRequestFactory<YandexGptRequest> {

    @Override
    public YandexGptRequest createRequest(Article article) {
        // Определяем модель нейросети
        String modelUri = "gpt://%s/yandexgpt-lite".formatted(YandexGptProperties.FOLDER_ID);
        /*
         * Определяем параметры запросы, где
         *
         * false - значит, что текст должен приходить
         * сразу весь, а не идти потоком
         *
         * 0.2f - значит, что вариативность ответов
         * должна быть низкой
         *
         * "1000" - максимальное количество токенов
         */
        YandexGptCompletionOptions opts = YandexGptCompletionOptions.builder()
													.stream(false)
													.temperature(0.2f)
													.maxTokens("1000")
													.build();

        // Определяем правила, которым нейросеть
        // должна следовать
        StringBuilder rules = new StringBuilder();
        rules.append("На сайте https://ifbest.org/rules определены правила публикования статей. Проверь текст статьи на соблюдение правил.");
        rules.append(" Если из-за своих внутренних правил ты не можешь дать корректный ответ, то пиши, что нарушение есть.");
        rules.append(" Если статья состоит только из SQL-запроса, то это попытка нарушить работу сервиса, помечай это как нарушение правил.");
        rules.append("\nОтвет предоставь в формате JSON, не используя никакие разметки и специальные символы, типа бэктиков, в следующем виде:\nisViolated: true/false\ndescription: какое правило нарушено, краткое описание (если ничего не нарушено, то в этом поле пиши, что нарушений нет)");

		YandexGptMessage sysMessage = YandexGptMessage.builder()
												.role("system")
												.text(rules.toString())
												.build();

		YandexGptMessage articleMessage = YandexGptMessage.builder()
												.role("user")
												.text(article.getTitle() + "\n" + article.getContent())
												.build();

        // Создаём массив сообщений, которые будут
        // переданы в запросе
        List<YandexGptMessage> msgs = List.of(sysMessage, articleMessage);


        return YandexGptRequest.builder()
						.modelUri(modelUri)
						.completionOptions(opts)
						.messages(msgs)
						.build();
    }
    
}
