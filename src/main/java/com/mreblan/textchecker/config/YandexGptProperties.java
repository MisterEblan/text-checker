package com.mreblan.textchecker.config;

import lombok.Data;

@Data
public class YandexGptProperties {
    public static final String API_KEY = System.getenv("YANDEX_API_KEY");
    public static final String FOLDER_ID = System.getenv("YANDEX_API_FOLDER");
	public static final String URI = System.getenv("YANDEX_URI");
}
