package com.mreblan.textchecker.models;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mreblan.textchecker.ai.YandexGptProperties;
import com.mreblan.textchecker.models.yandexgpt.YandexGptMessage;
import com.mreblan.textchecker.models.yandexgpt.YandexGptCompletionOptions;

import lombok.Data;

@Data
@Component
public class YandexGptRequest {
    private final YandexGptProperties  properties;
    private String                     modelUri;
    private YandexGptCompletionOptions completionOptions;
    private List<YandexGptMessage>     messages;

    @Autowired
    public YandexGptRequest(YandexGptProperties props) {
    this.properties = props;
  }
}
