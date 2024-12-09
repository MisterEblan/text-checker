package com.mreblan.textchecker.ai;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.Data;

@Data
public class YandexGptProperties {
    // @Value("${yandex.key.value}")
    public final static String API_KEY = System.getenv("YANDEX_API_KEY");

    // @Value("${yandex.folder.id}")
    public final static String FOLDER_ID = System.getenv("YANDEX_API_FOLDER");

}
