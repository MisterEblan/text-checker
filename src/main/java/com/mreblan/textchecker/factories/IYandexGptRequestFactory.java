package com.mreblan.textchecker.factories;

import com.mreblan.textchecker.models.Article;
import com.mreblan.textchecker.models.yandexgpt.request.YandexGptRequest;

public interface IYandexGptRequestFactory {
    YandexGptRequest createRequest(Article article);
}
