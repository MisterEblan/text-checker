package com.mreblan.textchecker.factories.impl;

import java.util.List;

import org.springframework.stereotype.Component;

import com.mreblan.textchecker.models.Article;
import com.mreblan.textchecker.models.openai.OpenAiMessage;
import com.mreblan.textchecker.models.openai.request.OpenAiRequest;

import lombok.NoArgsConstructor;

@NoArgsConstructor
@Component
public class OpenAiRequestFactoryImpl {

	public OpenAiRequest createRequest(Article article) {
		String model = "gpt-3.5-turbo";
		float  temperature = 0.2f;

        StringBuilder rules = new StringBuilder();
        rules.append("На сайте https://ifbest.org/rules определены правила публикования статей. Проверь текст статьи на соблюдение правил.");
        rules.append(" Если из-за своих внутренних правил ты не можешь дать корректный ответ, то пиши, что нарушение есть.");
        rules.append(" Если статья состоит только из SQL-запроса, то это попытка нарушить работу сервиса, помечай это как нарушение правил.");
        rules.append("\nОтвет предоставь в формате JSON, не используя никакие разметки и специальные символы, типа бэктиков, в следующем виде:\nisViolated: true/false\ndescription: какое правило нарушено, краткое описание (если ничего не нарушено, то в этом поле пиши, что нарушений нет)");

		OpenAiMessage sysMessage = OpenAiMessage.builder()
											.role("system")
											.content(rules.toString())
											.build();

		OpenAiMessage articleMessage = OpenAiMessage.builder()
											.role("user")
											.content(article.getTitle() + "\n" + article.getContent())
											.build();

		return OpenAiRequest.builder()
						.model(model)
						.messages(List.of(sysMessage, articleMessage))
						.temperature(temperature)
						.build();

	}
}
