package com.mreblan.textchecker.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

import com.mreblan.textchecker.factories.IGptRequestFactory;
import com.mreblan.textchecker.factories.impl.OpenAiRequestFactoryImpl;
import com.mreblan.textchecker.factories.impl.YandexGptRequestFactoryImpl;
import com.mreblan.textchecker.models.openai.request.OpenAiRequest;
import com.mreblan.textchecker.models.yandexgpt.request.YandexGptRequest;

@Configuration
public class AppConfig {

    @Bean
    public RestClient restClient() {
        return RestClient.builder()
            .baseUrl("")
            .defaultHeaders(
                httpHeader -> {
                    httpHeader.set("Content-Type", "application/json");
                })
            .build();
    }

	@Bean
	public IGptRequestFactory<YandexGptRequest> yandexRequestFactory() {
		return new YandexGptRequestFactoryImpl();
	}

	@Bean
	public IGptRequestFactory<OpenAiRequest> openAiRequestFactory() {
		return new OpenAiRequestFactoryImpl();
	}
}
