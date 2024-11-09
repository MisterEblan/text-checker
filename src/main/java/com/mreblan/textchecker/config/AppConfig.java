package com.mreblan.textchecker.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.mreblan.textchecker.ai.YandexGptProperties;
import com.mreblan.textchecker.factories.impl.YandexGptRequestFactoryImpl;

@Configuration
public class AppConfig {

    // @Value("${yandex.key.value}")
    // private String API_KEY;
    //
    // @Value("${yandex.folder.id}")
    // private String FOLDER_ID;

    @Bean
    public String string() {
        return "";
    }

    @Bean
    public boolean bool() {
      return false;
    }
    
    @Bean
    public float floatNum() {
        return 0.0f;
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
