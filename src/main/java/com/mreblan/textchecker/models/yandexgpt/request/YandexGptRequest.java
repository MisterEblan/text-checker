package com.mreblan.textchecker.models.yandexgpt.request;

import java.util.List;

import com.mreblan.textchecker.models.yandexgpt.YandexGptMessage;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class YandexGptRequest {
    private String modelUri;
    private YandexGptCompletionOptions completionOptions;
    private List<YandexGptMessage> messages;

}
