package com.mreblan.textchecker.models.yandexgpt.request;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class YandexGptCompletionOptions {
    private boolean stream;
    private float   temperature;
    private String  maxTokens;

}
