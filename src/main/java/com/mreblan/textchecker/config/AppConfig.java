package com.mreblan.textchecker.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
    @Bean
    public String modelUri() {
      return "gpt://b1gahi2sqctvfmqblvtl/yandexgpt/latest";
    }

    @Bean
    public boolean stream() {
      return false;
    }
    //
    @Bean
    public float temperature() {
      return 0.2f;
    }

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
