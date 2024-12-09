package com.mreblan.textchecker.models.yandexgpt.request;

import org.springframework.stereotype.Component;

import lombok.Data;

@Data
public class YandexGptCompletionOptions {
    private boolean stream;
    private float   temperature;
    private String  maxTokens;

    public YandexGptCompletionOptions(boolean stream, float temp, String maxTokens) {
        this.stream = stream;
        this.temperature = temp;
        this.maxTokens = maxTokens;
    }
}
