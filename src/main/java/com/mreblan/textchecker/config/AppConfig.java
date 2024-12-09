package com.mreblan.textchecker.config;

// import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mreblan.textchecker.ai.YandexGptProperties;
import com.mreblan.textchecker.factories.impl.OpenAiRequestFactoryImpl;
import com.mreblan.textchecker.models.openai.request.OpenAiRequest;
import com.mreblan.textchecker.services.IAiSender;
import com.mreblan.textchecker.services.impl.OpenAiService;

// import com.mreblan.textchecker.ai.YandexGptProperties;
// import com.mreblan.textchecker.factories.impl.YandexGptRequestFactoryImpl;

@Configuration
public class AppConfig {

    @Bean
    public RestClient restClient() {
        return RestClient.builder()
            .defaultHeaders(
                httpHeader -> {
                    httpHeader.set("Content-Type", "application/json");
                })
            .build();
    }

	@Bean
	public OpenAiRequestFactoryImpl openAiRequestFactoryImpl() {
		return new OpenAiRequestFactoryImpl();
	} 

	@Bean
	public ObjectMapper objectMapper() {
		return new ObjectMapper();
	}

    // @Bean
    // public YandexGptProperties yandexGptProperties() {
    //     return new YandexGptProperties(API_KEY, FOLDER_ID);
    // }
    //
    // @Bean
    // public YandexGptRequestFactoryImpl yandexGptRequestFactoryImpl(YandexGptProperties yandexGptProperties) {
    //     return new YandexGptRequestFactoryImpl(yandexGptProperties);
    // }

  //   @Bean
  //   public String maxTokens() {
  //     return "1000";
  // }

    // @Bean
    // public String role() { return "system"; }
    //
    // @Bean
    // public String text() { return "text"; }
}
