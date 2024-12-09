package com.mreblan.textchecker.models.yandexgpt.request;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mreblan.textchecker.models.yandexgpt.YandexGptMessage;
import com.mreblan.textchecker.models.yandexgpt.request.YandexGptCompletionOptions;

import lombok.Getter;

@Getter
@Component
public class YandexGptRequest {
    private String modelUri;
    private YandexGptCompletionOptions completionOptions;
    private List<YandexGptMessage> messages;

    @Autowired
    public void setModelUri(String modelUri) {
        this.modelUri = modelUri;
    }

    @Autowired
    public void setCompletionOptions(YandexGptCompletionOptions opts) {
        this.completionOptions = opts;
    }

    @Autowired
    public void setMessages(List<YandexGptMessage> msgs) {
        this.messages = msgs;
    }
}
