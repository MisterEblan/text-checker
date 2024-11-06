package com.mreblan.textchecker.models.yandexgpt;

import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
public class YandexGptCompletionOptions {
    private boolean stream;
    private float   temperature;
    private String  maxTokens;
}
