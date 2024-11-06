package com.mreblan.textchecker.ai;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
public class YandexGptProperties {
    @Value("${yandex.key.value}")
    private final String API_KEY;

    @Value("${yandex.folder.id}")
    private final String FOLDER_ID;
}
