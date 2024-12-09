package com.mreblan.textchecker.models.yandexgpt.request;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mreblan.textchecker.models.yandexgpt.YandexGptMessage;
import com.mreblan.textchecker.models.yandexgpt.request.YandexGptCompletionOptions;

import lombok.Getter;

@Getter
public class YandexGptRequest {
    private String modelUri;
    private YandexGptCompletionOptions completionOptions;
    private List<YandexGptMessage> messages;

}
